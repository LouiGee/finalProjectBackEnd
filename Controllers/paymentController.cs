using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Newtonsoft.Json;
using PaymentMicroservice.Data;

[ApiController]
[Route("api/payment")]
public class PaymentController : ControllerBase
{
    private readonly TinkPaymentIdService _paymentIdService;
    private readonly TinkBulkPaymentService _bulkPaymentService;
    private readonly PaymentRecordDbContext _contextPR;
    private readonly SupplierPaymentDetailsDbContext _contextSPD;

    public PaymentController(TinkPaymentIdService paymentIdService, TinkBulkPaymentService bulkPaymentService, PaymentRecordDbContext contextPR, SupplierPaymentDetailsDbContext contextSPD)
    {
        _paymentIdService = paymentIdService;
        _bulkPaymentService = bulkPaymentService;
        _contextPR = contextPR;
        _contextSPD = contextSPD;

    }

    [HttpPost("start")]
    public async Task<IActionResult> StartPayment([FromBody] List<PaymentRequest> request)
    {
        var paymentsIdList = new List<string> ();
        
        foreach (var paymentToCompanyJson in request)
        {

            //Fetch SortCodeAccountNumber from DB to then Paass intot he payment ID generator
            var company = await _contextSPD.SupplierPaymentDetails.FirstOrDefaultAsync(s => s.SupplierName == paymentToCompanyJson.Supplier);

            string paymentId = await _paymentIdService.CreatePaymentIdAsync(paymentToCompanyJson.TotalPriceAmount, paymentToCompanyJson.Supplier, company.SortCodeAccountNumber);

            paymentsIdList.Add(paymentId);
            
        }

        var paymentRequestIds = new Dictionary<string, List<string>>();

        // This is the body of what is to be sent to Tink Bulk Payment service cosisitng of a a list of payment ID's to individuals suppliers (may rename company to supplier later)
        paymentRequestIds["paymentRequestIds "] = paymentsIdList;

        string bankRedirectUrl = await _bulkPaymentService.CreateBulkPaymentAsync(paymentRequestIds);

        return Redirect(bankRedirectUrl);
    }

    [HttpGet("callback")]
    public IActionResult PaymentCallback([FromQuery] string paymentRequestId, [FromQuery] string status)
    {
        // Handle Tink's redirect after approval
        return Ok($"Payment {paymentRequestId} status: {status}");
    }
}
using Microsoft.AspNetCore.Mvc;
using PaymentMicroservice.Data;
using PaymentMicroservice.Domain;

namespace PaymentMicroservice.Controllers
{
    [ApiController]
    [Route("api/paymentRecord")]
    public class PaymentRecordController : ControllerBase
    {
        private readonly PaymentRecordDbContext _context;

        public PaymentRecordController(PaymentRecordDbContext context)
        {
            _context = context;
        }

        // POST: api/MakePayment
        [HttpPost("recordPayment")]
        public async Task<IActionResult> RecordPayment([FromBody] PaymentRecord payment)
        {
            if (!ModelState.IsValid)
                return BadRequest(ModelState);

            try
            {
                _context.PaymentRecords.Add(payment);
                await _context.SaveChangesAsync();

                return CreatedAtAction(nameof(RecordPayment), new { id = payment.PaymentItemId }, payment);
            }
            catch (Exception ex)
            {
                return StatusCode(500, $"Internal error: {ex.Message}");
            }
        }
    }
}
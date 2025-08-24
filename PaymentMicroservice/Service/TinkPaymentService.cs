using System.Net.Http.Headers;
using System.Text;
using Newtonsoft.Json;

public class TinkPaymentService
{
    private readonly HttpClient _httpClient;
    private readonly TinkAuthService _auth;

    public TinkPaymentService(HttpClient httpClient, TinkAuthService auth)
    {
        _httpClient = httpClient;
        _auth = auth;
    }

    public async Task<string> CreatePaymentAsync(decimal amountToBePaid, string company, long accountNumberFromDB)
    {
        var token = await _auth.GetAccessBulkTokenAsync();

        Console.WriteLine(token);

         var payload = new
        {
            destinations = new[]
            {
                new
                {
                    accountNumber = $"{accountNumberFromDB}",
                    type = "sort-code"
                }
            },
            amount = amountToBePaid,
            currency = "GBP",
            market = "GB",
            recipientName = company,
            paymentScheme = "FASTER_PAYMENTS",
            redirectURi ="https://048a7b680dbc.ngrok-free.app/payPurchaseOrder",

            remittanceInformation = new
            
            {
                type = "REFERENCE",
                value = "Payment Run"
            }
        };

        var json = JsonConvert.SerializeObject(payload);
        var request = new HttpRequestMessage(HttpMethod.Post, "https://api.tink.com/api/v1/payments/requests")
        {
            Content = new StringContent(json, Encoding.UTF8, "application/json")
        };
        request.Headers.Authorization = new AuthenticationHeaderValue("Bearer", token);

        var response = await _httpClient.SendAsync(request);
        var body = await response.Content.ReadAsStringAsync();

        if (!response.IsSuccessStatusCode)
            throw new Exception($"Payment creation failed: {body}");

        dynamic result = JsonConvert.DeserializeObject(body);
        string paymentId = result.id;

        
        //string clientPaymentRedirectUrl = $"https://link.tink.com/1.0/pay?client_id=f5b9f77cf90048308317e4e10f6d5365&redirect_uri=https://048a7b680dbc.ngrok-free.app/payPurchaseOrder&2Fcallback&market=GB&locale=en_US&payment_request_id={paymentId}";
         string clientPaymentRedirectUrl = $"https://link.tink.com/1.0/pay?client_id=f5b9f77cf90048308317e4e10f6d5365&redirect_uri=https%3A%2F%2Fconsole.tink.com%2Fcallback&market=GB&locale=en_US&payment_request_id={paymentId}";
        return clientPaymentRedirectUrl; 
    }
}
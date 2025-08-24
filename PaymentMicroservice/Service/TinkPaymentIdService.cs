/* Future adjustments */


/* 
using System.Net.Http.Headers;
using System.Text;
using Newtonsoft.Json;

public class TinkPaymentIdService
{
    private readonly HttpClient _httpClient;
    private readonly TinkAuthService _auth;

    public TinkPaymentIdService(HttpClient httpClient, TinkAuthService auth)
    {
        _httpClient = httpClient;
        _auth = auth;
    }

    public async Task<string> CreatePaymentIdAsync(decimal amountToBePaid, string company, long accountNumberFromDB)
    {
        var token = await _auth.GetAccessTokenAsync();

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
            redirectUri = "https://2fb942ae7a69.ngrok-free.app/payPurchaseOrder",
            remittanceInformation = new 
            
            {
                type = "REFERENCE",
                value = "Payment Run"
            }
        };
                




        



/*

                                        var payload = new
                                                {
                                                    recipients = new 
                                                    {
                                                        accountNumber = "IT60X0542811101000000123456",  

                                                        accountType = "iban"
                                                    },
                                                    amount = 100,
                                                    currency = "EUR",
                                                    market = "IT",
                                                    recipientName = "Test AB",
                                                    sourceMessage = "Payment for Gym Equipment",
                                                    remittanceInformation = new
                                                    {
                                                        type = "UNSTRUCTURED",
                                                        value = "CREDITOR REFERENCE"
                                                    },
                                                    paymentScheme = "SEPA_CREDIT_TRANSFER"
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
        // string clientPaymentRedirectUrl = $"https://link.tink.com/1.0/pay?client_id=f5b9f77cf90048308317e4e10f6d5365&redirect_uri=https%3A%2F%2Fconsole.tink.com%2Fcallback&market=GB&locale=en_US&payment_request_id={payment_id}";
        return paymentId; 
    }
}

*/
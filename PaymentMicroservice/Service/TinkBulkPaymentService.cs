/* Future adjustments */


/*
using System.Net.Http.Headers;
using System.Text;
using Newtonsoft.Json;

public class TinkBulkPaymentService
{
    private readonly HttpClient _httpClient;
    private readonly TinkAuthService _auth;

    public TinkBulkPaymentService(HttpClient httpClient, TinkAuthService auth)
    {
        _httpClient = httpClient;
        _auth = auth;
    }

    public async Task<string> CreateBulkPaymentAsync(Dictionary<string,List<string>> listOfPaymentIds)
    {
        var token = await _auth.GetAccessBulkTokenAsync();

        Console.WriteLine(token);
        

        var json = JsonConvert.SerializeObject(listOfPaymentIds);
        var request = new HttpRequestMessage(HttpMethod.Post, "https://api.tink.com/payment/v1/bulk-payments")
        {
            Content = new StringContent(json, Encoding.UTF8, "application/json")
        };
        request.Headers.Authorization = new AuthenticationHeaderValue("Bearer", token);

        var response = await _httpClient.SendAsync(request);
        var body = await response.Content.ReadAsStringAsync();

        if (!response.IsSuccessStatusCode)
            throw new Exception($"Payment creation failed: {body}");

        dynamic result = JsonConvert.DeserializeObject(body);
        string bulkPaymentId = result.id;
        string clientPaymentRedirectUrl = $"https://link.tink.com/1.0/bulk?client_id=f5b9f77cf90048308317e4e10f6d5365&redirect_uri=https%3A%2F%2Fconsole.tink.com%2Fcallback&market=GB&locale=en_US&payment_request_id={bulkPaymentId}";
        return clientPaymentRedirectUrl; 
    }
}

*/
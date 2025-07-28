using Newtonsoft.Json;

public class TinkAuthService
{
    private readonly HttpClient _httpClient;
    private readonly IConfiguration _config;

    public TinkAuthService(HttpClient httpClient, IConfiguration config)
    {
        _httpClient = httpClient;
        _config = config;
    }

    public async Task<string> GetAccessTokenAsync()
    {
        var content = new FormUrlEncodedContent(new[]
        {
            new KeyValuePair<string, string>("client_id", "f5b9f77cf90048308317e4e10f6d5365"),
            new KeyValuePair<string, string>("client_secret", "0dfe4c4107834e879a1d4cec48519a93"),
            new KeyValuePair<string, string>("grant_type", "client_credentials"),
            new KeyValuePair<string, string>("scope", "payment:read,payment:write")
        });

        var response = await _httpClient.PostAsync("https://api.tink.com/api/v1/oauth/token", content);
        var body = await response.Content.ReadAsStringAsync();

        if (!response.IsSuccessStatusCode)
            throw new Exception($"Tink Auth Failed: {body}");

        dynamic json = JsonConvert.DeserializeObject(body);
        return json.access_token;
    }

    public async Task<string> GetAccessBulkTokenAsync()
    {
        var content = new FormUrlEncodedContent(new[]
        {
            new KeyValuePair<string, string>("client_id", "f5b9f77cf90048308317e4e10f6d5365"),
            new KeyValuePair<string, string>("client_secret", "0dfe4c4107834e879a1d4cec48519a93"),
            new KeyValuePair<string, string>("grant_type", "client_credentials"),
            new KeyValuePair<string, string>("scope", "accounts:read,payment:read,payment:write")
        });

        var response = await _httpClient.PostAsync("https://api.tink.com/api/v1/oauth/token", content);
        var body = await response.Content.ReadAsStringAsync();

        if (!response.IsSuccessStatusCode)
            throw new Exception($"Tink Auth Failed: {body}");

        dynamic json = JsonConvert.DeserializeObject(body);
        return json.access_token;
    }
}
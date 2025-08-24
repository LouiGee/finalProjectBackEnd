public class PaymentRequest
{
    public required string Supplier { get; set; }
    public decimal TotalPriceAmount { get; set; }
    public required string PaidBy { get; set; }
    public required List<ItemAndPriceDto> ItemsAndPrice { get; set; }
}

public class ItemAndPriceDto
{
    public required string PoItemNumber { get; set; }
    public required string Item { get; set; }
    public decimal Price { get; set; }
}

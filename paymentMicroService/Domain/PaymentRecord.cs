using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace PaymentMicroservice.Domain
{
    
    [Table("payment_records")]
    public class PaymentRecord
    {

        public required string PaymentId { get; set; }

        [Key]
        public required string PaymentItemId { get; set; }

        public string? Item { get; set; }

        public decimal Amount { get; set; }

        public string? Supplier { get; set; }

        public string? PaidBy { get; set; }

        public DateTime DatePaid { get; set; }


    }
}
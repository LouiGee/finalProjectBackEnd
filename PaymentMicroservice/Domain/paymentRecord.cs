using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace PaymentMicroservice.Domain
{
    
    [Table("payment_records")]
    public class PaymentRecord
    {

        public int PaymentId { get; set; }

        [Key]
        public int PaymentItemId { get; set; }

        public int Item { get; set; }

        public int Amount { get; set; }

        public string? Supplier { get; set; }

        public string? PaidBy { get; set; }

        public DateTime DatePaid { get; set; }


    }
}
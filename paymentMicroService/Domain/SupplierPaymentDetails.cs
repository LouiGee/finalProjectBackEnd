using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace PaymentMicroservice.Domain
{
    
    [Table("supplier_payment_details")]
    public class SupplierPaymentDetails
    {

        [Key]
        public int SupplierId { get; set; }
 
        public required string SupplierName { get; set; }

        public long SortCodeAccountNumber { get; set; }


    }
}
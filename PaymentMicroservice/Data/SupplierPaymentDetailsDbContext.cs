using Microsoft.EntityFrameworkCore;
using PaymentMicroservice.Domain;

namespace PaymentMicroservice.Data
{
    public class SupplierPaymentDetailsDbContext : DbContext
    {
        public SupplierPaymentDetailsDbContext(DbContextOptions<SupplierPaymentDetailsDbContext> options)
            : base(options)
        {
        }

        public DbSet<SupplierPaymentDetails> SupplierPaymentDetails { get; set; }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            modelBuilder.Entity<SupplierPaymentDetails>().ToTable("supplier_payment_details");
        }
    }
}
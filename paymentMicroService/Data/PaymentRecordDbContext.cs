using Microsoft.EntityFrameworkCore;
using PaymentMicroservice.Domain;

namespace PaymentMicroservice.Data
{
    public class PaymentRecordDbContext : DbContext
    {
        public PaymentRecordDbContext(DbContextOptions<PaymentRecordDbContext> options)
            : base(options)
        {
        }

        public DbSet<PaymentRecord> PaymentRecords { get; set; }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            modelBuilder.Entity<PaymentRecord>().ToTable("payment_records");
        }

        public static implicit operator PaymentRecordDbContext(SupplierPaymentDetailsDbContext v)
        {
            throw new NotImplementedException();
        }
    }
}
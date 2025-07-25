using Microsoft.EntityFrameworkCore;
using PaymentMicroservice.Domain;

namespace PaymentMicroservice.Data
{
    public class PaymentDbContext : DbContext
    {
        public PaymentDbContext(DbContextOptions<PaymentDbContext> options)
            : base(options)
        {
        }

        public DbSet<PaymentRecord> PaymentRecords { get; set; }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            modelBuilder.Entity<PaymentRecord>().ToTable("payment_records");
        }
    }
}
using Microsoft.EntityFrameworkCore;
using PaymentMicroservice.Data;

var builder = WebApplication.CreateBuilder(args);

// Load connection string from appsettings.json
var connectionString = builder.Configuration.GetConnectionString("DefaultConnection");

// Register DbContext with MySQL provider
builder.Services.AddDbContext<PaymentDbContext>(options =>
    options.UseMySql(connectionString, new MySqlServerVersion(new Version(8, 0, 36))));

// Add controller services (to expose API endpoints)
builder.Services.AddControllers();



var app = builder.Build();

app.MapControllers();

app.Run();
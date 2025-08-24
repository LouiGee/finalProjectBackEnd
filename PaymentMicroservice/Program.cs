using Microsoft.EntityFrameworkCore;
using PaymentMicroservice.Data;
using PaymentMicroservice.Domain;

var builder = WebApplication.CreateBuilder(args);

// Define cors policy

builder.Services.AddCors(options =>
{
    options.AddPolicy("AllowFrontend", policy =>
    {
        policy.WithOrigins(
                "http://localhost:4000", 
                "http://raptor.kent.ac.uk:4000"
            )
            .AllowAnyHeader()
            .AllowAnyMethod()
            .AllowCredentials();
    });
});

// Load connection string from appsettings.json
var connectionString = builder.Configuration.GetConnectionString("DefaultConnection");

// Register DbContext with MySQL provider
builder.Services.AddDbContext<SupplierPaymentDetailsDbContext>(options =>
    options.UseMySql(connectionString, new MySqlServerVersion(new Version(8, 0, 36))));

// Add controller services (to expose API endpoints)
builder.Services.AddControllers();

builder.Services.AddControllersWithViews();
builder.Services.AddHttpClient();
builder.Services.AddScoped<TinkAuthService>();
builder.Services.AddScoped<TinkPaymentService>();


//Create a few suppliers 


async Task SeedDatabaseAsync(SupplierPaymentDetailsDbContext context)
{
    if (!await context.SupplierPaymentDetails.AnyAsync())
    {
        var supplierPaymentDetails = new[]
        {
            new SupplierPaymentDetails { SupplierName = "Acme Fruits/Veg" , SortCodeAccountNumber = 31245678901234},
            new SupplierPaymentDetails { SupplierName = "Kent Seasonings" , SortCodeAccountNumber = 12345671234567},

        };

        context.SupplierPaymentDetails.AddRange(supplierPaymentDetails);
        await context.SaveChangesAsync();
    }
}


var app = builder.Build();

// Seed the supplier database
using (var scope = app.Services.CreateScope())
{
    var services = scope.ServiceProvider;
    var supplierDbContext = services.GetRequiredService<SupplierPaymentDetailsDbContext>();
    await SeedDatabaseAsync(supplierDbContext);
}

// Use the CORS policy
app.UseCors("AllowFrontend");

// Config
app.UseHttpsRedirection();
app.UseStaticFiles();
app.UseRouting();
app.MapControllers();
app.Run();
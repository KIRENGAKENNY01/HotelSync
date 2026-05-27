package com.hotelmanagement.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";

        List<Tag> tags = new ArrayList<>();
        tags.add(new Tag().name("1. Authentication").description("Register a new account or login to get a JWT token"));
        tags.add(new Tag().name("2. Customer - Hotel Management").description("Browse available hotels"));
        tags.add(new Tag().name("3. Customer - Room Management").description("View rooms available in a hotel"));
        tags.add(new Tag().name("4. Customer - Booking Management").description("Step 1: Submit a booking request → status starts as PENDING until admin confirms"));
        tags.add(new Tag().name("5. Customer - Billing Management").description("Step 2: After admin confirms, pay your invoice here → room is locked once payment is made"));
        tags.add(new Tag().name("6. Admin - Dashboard Analytics").description("View system-wide statistics"));
        tags.add(new Tag().name("7. Admin - Hotel Management").description("Create, update and delete hotels"));
        tags.add(new Tag().name("8. Admin - Room Management").description("Add, update and delete rooms in hotels"));
        tags.add(new Tag().name("9. Admin - Booking Management").description("Step 1 (Admin): Review PENDING bookings and confirm or cancel them. Confirming generates an invoice and emails the customer"));
        tags.add(new Tag().name("10. Admin - Billing Management").description("View all billing records across the system"));

        return new OpenAPI()
                .info(new Info()
                        .title("Hotel Management System API")
                        .version("1.0")
                        .description("## Booking Flow\n\n" +
                                "**1. Register / Login** → get JWT token, click Authorize 🔒\n\n" +
                                "**2. Customer:** Browse hotels & rooms, then `POST /api/bookings` → booking created as **PENDING**\n\n" +
                                "**3. Admin:** `PUT /api/admin/bookings/{id}/confirm` → booking becomes **CONFIRMED**, invoice generated & emailed to customer\n\n" +
                                "**4. Customer:** `POST /api/billing/{id}/pay` → bill marked **PAID**, room locked, payment receipt emailed\n\n" +
                                "**5. Admin (optional):** `DELETE /api/admin/bookings/{id}` → booking **CANCELLED**, room freed, cancellation email sent"))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(
                        new Components()
                                .addSecuritySchemes(securitySchemeName,
                                        new SecurityScheme()
                                                .name(securitySchemeName)
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                )
                )
                .tags(tags);
    }
}

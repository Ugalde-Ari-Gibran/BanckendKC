package com.erp.BanckendKC.config;

import com.erp.BanckendKC.entity.Categoria;
import com.erp.BanckendKC.entity.Producto;
import com.erp.BanckendKC.entity.Usuario;
import com.erp.BanckendKC.enums.RolUsuario;
import com.erp.BanckendKC.repository.CategoriaRepository;
import com.erp.BanckendKC.repository.ProductoRepository;
import com.erp.BanckendKC.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final CategoriaRepository categoriaRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) {
        actualizarConstraintEstadoPago();
        inicializarAdmin();
        inicializarCategorias();
        inicializarProductos();
    }

    private void actualizarConstraintEstadoPago() {
        try {
            log.info("🔄 Actualizando constraint pedidos_estado_pago_check...");
            // Intentar eliminar la constraint antigua si existe
            try {
                jdbcTemplate.execute("ALTER TABLE pedidos DROP CONSTRAINT IF EXISTS pedidos_estado_pago_check");
            } catch (Exception e) {
                log.warn("No se pudo eliminar constraint (puede que no exista): " + e.getMessage());
            }
            
            // Crear la nueva constraint con CREDITO incluido
            jdbcTemplate.execute("ALTER TABLE pedidos ADD CONSTRAINT pedidos_estado_pago_check CHECK (estado_pago IN ('PENDIENTE', 'LIQUIDADO', 'CREDITO'))");
            log.info("✅ Constraint pedidos_estado_pago_check actualizada correctamente");
        } catch (Exception e) {
            log.error("⚠️ Error actualizando constraint: " + e.getMessage());
        }
    }

    private void inicializarAdmin() {
        if (!usuarioRepository.existsByEmail("admin@kamycarnes.com")) {
            Usuario admin = Usuario.builder()
                    .nombre("Admin Kamy Carnes")
                    .email("admin@kamycarnes.com")
                    .password(passwordEncoder.encode("admin123"))
                    .rol(RolUsuario.ADMIN)
                    .telefono("5555555555")
                    .build();
            usuarioRepository.save(admin);
            log.info("✅ Admin creado: admin@kamycarnes.com / admin123");
        }
    }

    private void inicializarCategorias() {
        if (categoriaRepository.count() == 0) {
            categoriaRepository.save(Categoria.builder().nombre("Res").icono("🥩").descripcion("Cortes de res").build());
            categoriaRepository.save(Categoria.builder().nombre("Pollo").icono("🍗").descripcion("Piezas de pollo").build());
            categoriaRepository.save(Categoria.builder().nombre("Cerdo").icono("🐖").descripcion("Cortes de cerdo").build());
            log.info("✅ Categorías creadas: Res, Pollo, Cerdo");
        }
    }

    private void inicializarProductos() {
        if (productoRepository.count() == 0) {
            Categoria res = categoriaRepository.findByActivaTrue().stream()
                    .filter(c -> c.getNombre().equals("Res")).findFirst().orElse(null);
            Categoria pollo = categoriaRepository.findByActivaTrue().stream()
                    .filter(c -> c.getNombre().equals("Pollo")).findFirst().orElse(null);
            Categoria cerdo = categoriaRepository.findByActivaTrue().stream()
                    .filter(c -> c.getNombre().equals("Cerdo")).findFirst().orElse(null);

            if (res != null) {
                productoRepository.save(crearProducto("Bistec", "Bistec de res", new BigDecimal("180.00"), new BigDecimal("195.00"), res));
                productoRepository.save(crearProducto("Molida", "Carne molida de res", new BigDecimal("160.00"), new BigDecimal("175.00"), res));
                productoRepository.save(crearProducto("Costilla", "Costilla de res", new BigDecimal("170.00"), new BigDecimal("185.00"), res));
                productoRepository.save(crearProducto("Arrachera", "Arrachera marinada", new BigDecimal("220.00"), new BigDecimal("240.00"), res));
            }

            if (pollo != null) {
                productoRepository.save(crearProducto("Pechuga", "Pechuga de pollo", new BigDecimal("90.00"), new BigDecimal("100.00"), pollo));
                productoRepository.save(crearProducto("Pierna/Muslo", "Pierna y muslo de pollo", new BigDecimal("80.00"), new BigDecimal("90.00"), pollo));
                productoRepository.save(crearProducto("Alitas", "Alitas de pollo", new BigDecimal("85.00"), new BigDecimal("95.00"), pollo));
                productoRepository.save(crearProducto("Pollo entero", "Pollo entero limpio", new BigDecimal("75.00"), new BigDecimal("85.00"), pollo));
            }

            if (cerdo != null) {
                productoRepository.save(crearProducto("Chuleta", "Chuleta de cerdo", new BigDecimal("130.00"), new BigDecimal("145.00"), cerdo));
                productoRepository.save(crearProducto("Costilla", "Costilla de cerdo", new BigDecimal("140.00"), new BigDecimal("155.00"), cerdo));
                productoRepository.save(crearProducto("Molida", "Carne molida de cerdo", new BigDecimal("120.00"), new BigDecimal("135.00"), cerdo));
                productoRepository.save(crearProducto("Lomo", "Lomo de cerdo", new BigDecimal("150.00"), new BigDecimal("165.00"), cerdo));
            }

            log.info("✅ Productos iniciales creados (12 productos)");
        }
    }

    private Producto crearProducto(String nombre, String descripcion,
                                    BigDecimal precioContado, BigDecimal precioParcial,
                                    Categoria categoria) {
        return Producto.builder()
                .nombre(nombre)
                .descripcion(descripcion)
                .precioContadoKg(precioContado)
                .precioParcialKg(precioParcial)
                .categoria(categoria)
                .activo(true)
                .build();
    }
}

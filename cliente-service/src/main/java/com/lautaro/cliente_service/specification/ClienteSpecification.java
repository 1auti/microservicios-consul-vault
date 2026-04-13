package com.lautaro.cliente_service.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.lautaro.cliente_service.model.Cliente;

public class ClienteSpecification {

    public static class ClienteSpecBuilder {

        private final List<Specification<Cliente>> specifications = new ArrayList<>();

        public ClienteSpecBuilder with(String campo, String valor) {
            if (valor != null && !valor.isBlank()) {
                specifications
                        .add((root, query, cb) -> cb.like(cb.lower(root.get(campo)), "%" + valor.toLowerCase() + "%"));
            }
            return this;
        }

        public ClienteSpecBuilder withExact(String campo, Object valor) {
            if (valor != null) {
                // Si es string y está vacío, no agregar
                if (valor instanceof String && ((String) valor).isBlank()) {
                    return this;
                }
                specifications.add((root, query, cb) -> cb.equal(root.get(campo), valor));
            }
            return this;
        }

        public Specification<Cliente> build() {
            return specifications.stream()
                    .reduce(Specification::and)
                    .orElse((root, query, cb) -> cb.conjunction());
        }
    }

    public static ClienteSpecBuilder builder() {
        return new ClienteSpecBuilder();
    }
}
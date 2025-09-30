package cruds.Pets.V2.infrastructure.gateway;

import cruds.Pets.V2.core.adapter.OngGateway;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PetOngGatewayAdapter implements OngGateway {

    private final cruds.Ong.V2.core.adapter.OngGateway ongGatewayV2;

    public PetOngGatewayAdapter(cruds.Ong.V2.core.adapter.OngGateway ongGatewayV2) {
        this.ongGatewayV2 = ongGatewayV2;
    }

    @Override
    public boolean existePorId(UUID id) {
        return ongGatewayV2.existePorId(id);
    }
}
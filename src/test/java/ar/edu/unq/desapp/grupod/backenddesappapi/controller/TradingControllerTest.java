package ar.edu.unq.desapp.grupod.backenddesappapi.controller;

import ar.edu.unq.desapp.grupod.backenddesappapi.controller.dtos.asset_advertisement.AssetAdvertisementCreationDTO;
import ar.edu.unq.desapp.grupod.backenddesappapi.controller.dtos.asset_advertisement.AssetAdvertisementDTO;
import ar.edu.unq.desapp.grupod.backenddesappapi.controller.dtos.user.UserDTO;
import ar.edu.unq.desapp.grupod.backenddesappapi.model.AssetAdvertisementType;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class TradingControllerTest extends ControllerTest {

    @Test
    public void ____() {
        var pepeGomezDTO = registerPepeGomez();
        var assetAdvertisementCreationDTO = new AssetAdvertisementCreationDTO(AssetAdvertisementType.BUY_ADVERTISEMENT, pepeGomezDTO.id(), "BNB", 10, 100.0);

        var assetAdvertisementDTO = postAssetAdvertisement(assetAdvertisementCreationDTO);

        assertThat(assetAdvertisementDTO.assetSymbol()).isEqualTo(assetAdvertisementCreationDTO.assetSymbol());
    }

    private AssetAdvertisementDTO postAssetAdvertisement(AssetAdvertisementCreationDTO assetAdvertisementCreationDTO) {
        return restTemplate.postForObject(assetAdvertisementURL(), new HttpEntity(assetAdvertisementCreationDTO), AssetAdvertisementDTO.class);
    }

}

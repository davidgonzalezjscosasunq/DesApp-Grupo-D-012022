package ar.edu.unq.desapp.grupod.backenddesappapi.controller;

import ar.edu.unq.desapp.grupod.backenddesappapi.controller.dtos.*;
import ar.edu.unq.desapp.grupod.backenddesappapi.factories.UserTestFactory;
import ar.edu.unq.desapp.grupod.backenddesappapi.model.AssetAdvertisementType;
import ar.edu.unq.desapp.grupod.backenddesappapi.persistence.AssetAdvertisementsRepository;
import ar.edu.unq.desapp.grupod.backenddesappapi.persistence.TransactionsRepository;
import ar.edu.unq.desapp.grupod.backenddesappapi.persistence.UserRepository;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.time.LocalDateTime;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract public class ControllerTest {

    private static WireMockServer binanceMockServer;
    private static WireMockServer dollarToPesoConversionRateMockServer;

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
        // Esto se agrego para desactivar la autenticacion para los tests de controllers y que funcionen en la demo con Postman
        // Lo ideal seria modificar todos los tests de controllers y todos los controllers para que esten autenticados
        dynamicPropertyRegistry.add("security.bypassAuthentication", () -> true);
        dynamicPropertyRegistry.add("api_dollar_to_peso_conversion_ratio_url", dollarToPesoConversionRateMockServer::baseUrl);
        dynamicPropertyRegistry.add("api_binance_base_url", binanceMockServer::baseUrl);
    }

    @LocalServerPort
    protected int port;

    @Autowired
    protected TestRestTemplate restTemplate;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AssetAdvertisementsRepository assetAdvertisementsRepository;

    @Autowired
    TransactionsRepository transactionsRepository;

    @AfterEach
    void tearDown() {
        transactionsRepository.deleteAll();
        assetAdvertisementsRepository.deleteAll();
        userRepository.deleteAll();
    }

    @BeforeAll
    static void beforeAll() {
        startMockedServers();
    }

    @AfterAll
    static void afterAll() {
        stopMockedServers();
    }

    static public void startMockedServers() {
        binanceMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig().dynamicPort());
        binanceMockServer.start();

        dollarToPesoConversionRateMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig().dynamicPort());
        dollarToPesoConversionRateMockServer.start();
    }

    static public void stopMockedServers() {
        binanceMockServer.stop();
        dollarToPesoConversionRateMockServer.stop();
    }

    public void mockServerToRespondWithDollarToPesoRatioOf(Float dollarToPesoRatioToReturn) {
        var dollarToPesoRatioToReturnAsString = dollarToPesoRatioToReturn.toString().replace('.', ',');

        dollarToPesoConversionRateMockServer.stubFor(get("/")
                .willReturn(
                        aResponse()
                                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                .withBody("[{\"casa\": {\"nombre\": \"Blue\", \"venta\": \"" + dollarToPesoRatioToReturnAsString + "\"}}]")));
    }

    public void mockServerToRespondWithSymbolPrice(String symbol, Float assetPrice) {
        binanceMockServer.stubFor(get("/api/v3/ticker/price?symbol=" + symbol)
                .willReturn(
                        aResponse()
                                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                .withBody("{\"symbol\": \"BTC\", \"price\": " +  assetPrice + "}")));
    }


    protected String baseURL() {
        return "http://localhost:" + port;
    }

    protected String userURL() {
        return baseURL() + "/users";
    }

    protected String userURLWithId(Long userId) {
        return userURL() + "/" + userId;
    }

    protected String assetAdvertisementURL() {
        return baseURL() + "/advertisements";
    }

    protected String transactionURL() {
        return assetAdvertisementURL() + "/transactions";
    }

    protected String transactionInformedByUserURL(Long userId) {
        return transactionURL() + "/users/" + userId;
    }

    protected String tradedVolumeByUserURL(Long userId) {
        return assetAdvertisementURL() + "/tradedVolume/users/" + userId;
    }

    protected UserRegistrationDTO createPepeGomezRegistrationDTO() {
        return new UserRegistrationDTO(UserTestFactory.PEPE_FIRST_NAME, UserTestFactory.PEPE_LAST_NAME, UserTestFactory.PEPE_EMAIL, UserTestFactory.PEPE_ADDRESS, UserTestFactory.PEPE_PASSWORD, UserTestFactory.PEPE_CVU, UserTestFactory.PEPE_CRIPTO_WALLET_ADDRESS);
    }

    protected UserRegistrationDTO createJuanPerezRegistrationDTO() {
        return new UserRegistrationDTO(UserTestFactory.JUAN_FIRST_NAME, UserTestFactory.JUAN_LAST_NAME, UserTestFactory.JUAN_EMAIL, UserTestFactory.JUAN_ADDRESS, UserTestFactory.JUAN_PASSWORD, UserTestFactory.JUAN_CVU, UserTestFactory.JUAN_CRIPTO_WALLET_ADDRESS);
    }

    protected UserDTO registerUser(UserRegistrationDTO userRegistrationDTO) {
        return restTemplate.postForObject(userURL(), new HttpEntity(userRegistrationDTO), UserDTO.class);
    }

    protected UserDTO registerPepeGomez() {
        return registerUser(createPepeGomezRegistrationDTO());
    }

    protected UserDTO registerJuanPerez() {
        return registerUser(createJuanPerezRegistrationDTO());
    }

    protected UserDTO findUserById(Long userId) {
        return restTemplate.getForObject(userURLWithId(userId), UserDTO.class);
    }

    protected AssetAdvertisementDTO postAssetAdvertisementFor(UserDTO userDTO) {
        return postAssetAdvertisement(createAssetAdvertisementCreationDTOFor(userDTO)).getBody();
    }
    
    protected ResponseEntity<AssetAdvertisementDTO> postAssetAdvertisement(PostAdvertisementCreationDTO assetAdvertisementCreationDTO) {
        return restTemplate.postForEntity(assetAdvertisementURL(), new HttpEntity(assetAdvertisementCreationDTO), AssetAdvertisementDTO.class);
    }

    protected ResponseEntity<AssetAdvertisementDTO[]> findAdvertisementsWithSymbol(String assetSymbol) {
        return restTemplate.getForEntity("/advertisements/" + assetSymbol, AssetAdvertisementDTO[].class);
    }

    protected ResponseEntity<AssetAdvertisementDTO[]> findBuyAdvertisementsWithSymbol(String assetSymbol) {
        return restTemplate.getForEntity("/advertisements/buy/" + assetSymbol, AssetAdvertisementDTO[].class);
    }

    protected ResponseEntity<AssetAdvertisementDTO[]> findSellAdvertisementsWithSymbol(String assetSymbol) {
        return restTemplate.getForEntity("/advertisements/sell/" + assetSymbol, AssetAdvertisementDTO[].class);
    }

    protected ResponseEntity<TransactionDTO> informTransaction(UserDTO userDTO, AssetAdvertisementDTO assetAdvertisementDTO) {
        return informTransaction(userDTO, assetAdvertisementDTO, assetAdvertisementDTO.quantity());
    }

    protected ResponseEntity<TransactionDTO> informTransaction(UserDTO userDTO, AssetAdvertisementDTO assetAdvertisementDTO, Integer quantity) {
        var transactionCreationDTO = new InformTransactionDTO(userDTO.id(), assetAdvertisementDTO.id(), quantity);
        return restTemplate.postForEntity(transactionURL(), transactionCreationDTO, TransactionDTO.class);
    }

    protected ResponseEntity<TransactionDTO> confirmTransaction(UserDTO userDTO, TransactionDTO transactionDTO) {
        var updateTransactionDTO = UpdateTransactionDTO.confirmationFor(userDTO, transactionDTO);

        return restTemplate.exchange(transactionURL(), HttpMethod.PUT, new HttpEntity<UpdateTransactionDTO>(updateTransactionDTO), TransactionDTO.class);
    }

    protected ResponseEntity<TransactionDTO> cancelTransaction(UserDTO userDTO, TransactionDTO transactionDTO) {
        var updateTransactionDTO = UpdateTransactionDTO.cancellationFor(userDTO, transactionDTO);

        return restTemplate.exchange(transactionURL(), HttpMethod.PUT, new HttpEntity<UpdateTransactionDTO>(updateTransactionDTO), TransactionDTO.class);
    }

    protected ResponseEntity<TransactionDTO[]> findTransactionsInformedBy(UserDTO userDTO) {
        return restTemplate.getForEntity(transactionInformedByUserURL(userDTO.id()), TransactionDTO[].class);
    }

    protected ResponseEntity<TradedVolumeDTO> getTradedVolumeBy(UserDTO userDTO, TradedVolumeBodyDTO tradedVolumeBodyDTO){
        return restTemplate.exchange(tradedVolumeByUserURL(userDTO.id()), HttpMethod.GET, new HttpEntity<TradedVolumeBodyDTO>(tradedVolumeBodyDTO), TradedVolumeDTO.class);
    }

    protected PostAdvertisementCreationDTO createAssetAdvertisementCreationDTOFor(UserDTO userDTO) {
        return createBuyAssetAdvertisementCreationDTOFor(userDTO);
    }

    protected PostAdvertisementCreationDTO createBuyAssetAdvertisementCreationDTOFor(UserDTO userDTO) {
        return new PostAdvertisementCreationDTO(AssetAdvertisementType.BUY_ADVERTISEMENT, userDTO.id(), "BNB", 10, 100.0);
    }

    protected PostAdvertisementCreationDTO createSellAssetAdvertisementCreationDTOFor(UserDTO userDTO) {
        return new PostAdvertisementCreationDTO(AssetAdvertisementType.SELL_ADVERTISEMENT, userDTO.id(), "BNB", 10, 100.0);
    }

    protected TradedVolumeBodyDTO createTradedVolumeBodyDTO(){
        return new TradedVolumeBodyDTO(LocalDateTime.parse("2021-12-30T19:34:50.63"), LocalDateTime.now());
    }
}

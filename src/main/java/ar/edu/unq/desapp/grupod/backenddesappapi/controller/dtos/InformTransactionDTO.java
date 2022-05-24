package ar.edu.unq.desapp.grupod.backenddesappapi.controller.dtos;

public class InformTransactionDTO {

    private Long interestedUserId;
    private int quantityToTransfer;

    public Long interestedUserId() {
        return interestedUserId;
    }

    public int quantityToTransfer() {
        return quantityToTransfer;
    }

}

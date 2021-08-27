package ch.qarts.tattool.repository;

public abstract class Mapper<D, M> {

    public abstract D toDTO(M domain);

    public abstract M toModel();

}

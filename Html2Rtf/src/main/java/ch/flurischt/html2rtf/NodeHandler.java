package ch.flurischt.html2rtf;

public interface NodeHandler<I, O> {

	public O handle(I input);

}
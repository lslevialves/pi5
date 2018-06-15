package negocio;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import beans.FormaPagamento;
import beans.ItemPedido;
import beans.Pedido;
import beans.Produto;
import beans.Status;
import persistencia.PedidoDao;
import persistencia.ProdutoDAO;

@ManagedBean
@ViewScoped
public class CarrinhoComprasController {

    private List<ItemPedido> carrinhoCompras;
    private List<ItemPedido> itens;
    private FormaPagamento formaPagamento;

    @PostConstruct
    public void inicializar(){
        inicializarListaItens();
    }

    public void inicializarListaItens(){
        List<Produto> produtos = new ProdutoDAO().listar();
        for (Produto prod:produtos) {
            ItemPedido item = new ItemPedido();
            item.setProduto(prod);
            getItens().add(item);
        }

        verificarSeExistemitensNasessao();
    }

    public List<ItemPedido> obterCarrinhoDeComprasNaSessao(){
        FacesContext currentInstance = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) currentInstance.getExternalContext().getSession(true);
        Object carrinhoCompras = session.getAttribute("carrinhoCompras");
        if (carrinhoCompras != null) {
            return (List<ItemPedido>) carrinhoCompras;
        }

        return null;
    }

    public void verificarSeExistemitensNasessao(){
        List<ItemPedido> carrinhoCompras = obterCarrinhoDeComprasNaSessao();

        if (carrinhoCompras != null ) {
            this.carrinhoCompras = carrinhoCompras;
            return;
        }

        FacesContext currentInstance = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) currentInstance.getExternalContext().getSession(true);
        session.setAttribute("carrinhoCompras", getCarrinhoCompras());

    }

    public void adicionarItemAoCarrinhoDeCompras(ItemPedido itemPedido){
        if (itemPedido.getQuantidade() == null || itemPedido.getQuantidade() <= 0){
            FacesContext.getCurrentInstance().
                    addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_INFO,
                                    "Adicione uma quantidade para inserir o produto",
                                      "Adicione uma quantidade para inserir o produto"));
            return;
        }

        Double valorTotal = itemPedido.getProduto().getPreco() * itemPedido.getQuantidade();
        itemPedido.setValorTotal(valorTotal);
        getCarrinhoCompras().add(itemPedido);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Produto Adicionado", "Produto Adicionado"));
    }

    public void removerItemCarrinhoCompras(ItemPedido item) {
        getCarrinhoCompras().remove(item);
        obterTotalCompra();
    }

    public FormaPagamento[] listaFormasPagamento(){
        return FormaPagamento.values();
    }

    public Double obterTotalCompra() {
        Double total = 0.0;
        for (ItemPedido item: getCarrinhoCompras()) {
            total += item.getValorTotal();
        }

        return total;
    }

    public void finalizarCompra() throws IOException, InterruptedException {
        Pedido pedido = new Pedido();
        pedido.setFormaPagamento(getFormaPagamento());
        pedido.setItens(getCarrinhoCompras());
        pedido.setStatus(Status.FINALIZADO);

        Double valorTotal = 0.0;

        for(ItemPedido item: getCarrinhoCompras()){
           valorTotal += item.getValorTotal();
        }

        pedido.setValorTotal(valorTotal);

        new PedidoDao().salva(pedido);

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                FacesMessage.SEVERITY_INFO,
                "Pedido finalizado com sucesso",
                "Pedido finalizado com sucesso"));

        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(true);
        session.setAttribute("pessoa", null);

        Thread.sleep(3000);
        externalContext.redirect("itens.xhtml");
    }
    
    public void finalizarCarrinhoDeCompras() throws IOException {
        FacesContext currentInstance = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) currentInstance.getExternalContext().getSession(true);

        Object pessoa = session.getAttribute("pessoa");
        if (pessoa == null){
            FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
        }
        FacesContext.getCurrentInstance().getExternalContext().redirect("confirmar-pedido.xhtml");
    }

    public List<ItemPedido> getCarrinhoCompras() {
        if (this.carrinhoCompras == null ){
            this.carrinhoCompras = new ArrayList<ItemPedido>();
        }
        return carrinhoCompras;
    }

    public void setCarrinhoCompras(List<ItemPedido> carrinhoCompras) {
        this.carrinhoCompras = carrinhoCompras;
    }

    public List<ItemPedido> getItens() {
        if (this.itens == null) {
            this.itens = new ArrayList<ItemPedido>();
        }
        return itens;
    }

    public void setItens(List<ItemPedido> itens) {
        this.itens = itens;
    }

    public FormaPagamento getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(FormaPagamento formaPagamento) {
        this.formaPagamento = formaPagamento;
    }
}

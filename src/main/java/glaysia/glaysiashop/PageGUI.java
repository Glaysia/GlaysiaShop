package glaysia.glaysiashop;

import com.github.stefvanschie.inventoryframework.font.util.Font;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;

import static java.lang.Math.abs;

public class PageGUI extends MyChestGui {

    private Material material;
    private PaginatedPane paginatedPane;
    public ItemPaletteGUI itemPaletteGUI = null;
    private Player player;
    private Economy econ;
    private Trade trade = new Trade();
    List<Trade.Order> shoppingList;
    List<Trade.Order> MyShoppingList;
    private MyLabel sign;
    private MyLabel sell;
    private MyLabel moneyLabel;
    private MyLabel amountItem10;
    private MyLabel goToPallete;
    private MyLabel goToAskingOrder;
    private MyLabel goToOrderBook;
    private MyLabel goToShoppingBook;
    private MyLabel goToMyShoppingBook;
    private int n; //요청된 거래 창 개수
    private int k;
    private int m;
    PageGUI(ItemPaletteGUI itemPaletteGUI, Player player, Economy econ){
        super("InitialTitle");
        this.itemPaletteGUI=itemPaletteGUI;
        this.player=player;
        this.econ=econ;
    }
    private void setTitleToAskingOrder(){
        this.setTitle("거래요청 창");
    }
    private void setTitleToOrderBook(){
        this.setTitle("호가 창");
    }
    private void setTitleToShoppingBook(){this.setTitle("요청리스트");}
    private void setTitleToMyShoppingBook(){
        this.setTitle("내 요청리스트");
    }
    private void setGUIToAskingOrder(@NotNull Material material){
        this.material = material;
        setGUIToAskingOrder();
    }
    /** 아이템선택 창으로 이동하기 */
    private void setGUIToPallete(){
        this.player.closeInventory();
        this.itemPaletteGUI.show(player);
    }

    /** 거래요청 창으로 이동하기 */
    private void setGUIToAskingOrder(){
        this.setTitleToAskingOrder();
//        this.paginatedPane.clear();
        paginatedPane = new PaginatedPane(0,0,9,6);
        player.sendMessage(String.format("당신이 고른 것은 %s입니다.", material));

        MyPane tradingItemPane = new MyPane(2, 3, 1, 1, material,"거래품목");
        tradingItemPane.preventTakeItemOnly(this);

        moneyLabel          = new MyLabel(1,0,7,1,Font.BIRCH_PLANKS,"0010000",  "가격");
        sign                = new MyLabel(0,0,1,1,Font.SPRUCE_PLANKS,"+",        "부호");
        MyLabel unit                = new MyLabel(8,0,1,1,Font.SPRUCE_PLANKS,"$",        "$");
        sell                = new MyLabel(0,1,1,1,Font.SPRUCE_PLANKS,"S",        "판매요청");
        amountItem10 = new MyLabel(3,3,1,2,Font.BIRCH_PLANKS,"64",       "거래개수");
        MyLabel incrementItem10         = new MyLabel(4,3,1,1,Font.RED,         "+",        "10개 증가");
        MyLabel incrementItem1          = new MyLabel(4,4,1,1,Font.PINK,        "+",        "1개 증가");
        MyLabel decrementItem10         = new MyLabel(5,3,1,1,Font.BLUE,        "-",        "10개 감소");
        MyLabel decrementItem1          = new MyLabel(5,4,1,1,Font.LIGHT_BLUE,  "-",        "1개 감소");
        MyLabel confirm1            = new MyLabel(6,4,1,1,Font.LIGHT_BLUE,  "C",        "거래요청하기");
        MyLabel confirm2_blue       = new MyLabel(7,4,1,1,Font.BLUE,        "C",        "거래요청확정하기(취소가능)");
        MyLabel confirm2_gray       = new MyLabel(7,4,1,1,Font.LIGHT_GRAY,  "C",        "..");

        confirm2_blue.setVisible(false);
        confirm2_gray.setVisible(true);
        confirm2_blue.setPriority(Pane.Priority.LOW);
        confirm2_gray.setPriority(Pane.Priority.HIGH);

        goToPallete         = new MyLabel(2,5,1,1,Font.RED,         "A",        "아이템선택 창으로 이동");
        goToAskingOrder     = new MyLabel(3,5,1,1,Font.STONE,       "B",        "거래요청 창으로 이동");
        goToAskingOrder.setVisible(false);
        goToOrderBook       = new MyLabel(4,5,1,1,Font.STONE,       "C",        "호가창으로 이동");
        goToShoppingBook    = new MyLabel(5,5,1,1,Font.STONE,       "D",        "거래요청 보기");
        goToMyShoppingBook  = new MyLabel(6,5,1,1,Font.STONE,       "E",        "내 거래요청 보기");

        PlusMinusButton incrementMoney=new PlusMinusButton(true);
        PlusMinusButton decrementMoney=new PlusMinusButton(false);


        paginatedPane.addPane(0, tradingItemPane);
        paginatedPane.addPane(0, moneyLabel);
        paginatedPane.addPane(0, sign);
        paginatedPane.addPane(0, unit);
        paginatedPane.addPane(0, sell);
        paginatedPane.addPane(0, amountItem10);
        paginatedPane.addPane(0, incrementItem10);
        paginatedPane.addPane(0, incrementItem1);
        paginatedPane.addPane(0, decrementItem10);
        paginatedPane.addPane(0, decrementItem1);
        paginatedPane.addPane(0, confirm1);
        paginatedPane.addPane(0, confirm2_blue);
        paginatedPane.addPane(0, confirm2_gray);

        this.addUnderBarButtonToPage(0);

        this.addPlusMinusButtonToPaginatedPane(0, incrementMoney);
        this.addPlusMinusButtonToPaginatedPane(0, decrementMoney);
        this.askingOrderClickEvent(confirm1, confirm2_blue, confirm2_gray, incrementItem10, incrementItem1, decrementItem10, decrementItem1, incrementMoney, decrementMoney);
        this.addPane(paginatedPane);
        this.update(player, econ);
        this.show(player);
    }

    /** 호가창으로 이동하기*/
    private void setGUIToOrderBook(){
        this.setTitleToOrderBook();
        this.paginatedPane.clear();

        MyPane itemPane = new MyPane(4,5,1,1,material,material.toString());
        this.addUnderBarButtonToPage(1);

        this.orderBookClickEvent(itemPane);

        AskPrice askPrice = new AskPrice(this);

        paginatedPane.setPage(1);
        this.update(player, econ);
    }

    /** 거래요청 리스트로 이동하기 */
    private void setGUIToShoppingBook(){
        this.setTitleToShoppingBook();
        this.paginatedPane.clear();

        int length = this.getShoppingListAndGetSizeOfList();
        MyPane nextPage = new MyPane(8,5,1,1,Material.ACACIA_BUTTON,"다음 페이지로");
        MyPane backPage = new MyPane(0,5,1,1,Material.ACACIA_BUTTON,"이전 페이지로");
        MyPane tmp;
        n = (length/45);

//        String maxPage=Integer.toString(n+1);
//        String curPage=Integer.toString(1);
//        String pageIn = "("+curPage+"/"+maxPage+")";
//
//        nextPage.setShowname("다음 페이지로"+pageIn);
//        backPage.setShowname("이전 페이지로"+pageIn);

        int idx=0;
        List<MyPane> panesOnShoppingBook = new LinkedList<>();
        for(int i=0;i<=n;i++){
            this.addUnderBarButtonToPage(2+i);
            for(int row=0;row<5;row++){
                if(idx==length)
                    break;
                for(int col=0;col<9;col++){
                    if(idx==length)
                        break;
                    tmp=new MyPane(col,row,1,1,(this.shoppingList.get(idx).material), this.shoppingList.get(idx), this.shoppingList.get(idx).toString());
                    panesOnShoppingBook.add(tmp);
                    tmp.setAmount(this.shoppingList.get(idx).amount);
                    paginatedPane.addPane(2+i, tmp);
                    paginatedPane.addPane(2+i, nextPage);
                    paginatedPane.addPane(2+i, backPage);
                    idx++;
                }
            }
        }
        this.shoppingBookClickEvent(panesOnShoppingBook, nextPage, backPage);
        this.paginatedPane.setPage(2);
        this.update(player, econ);
    }

    /** 내 거래요청 리스트로 이동하기 */
    private void setGUIToMyShoppingBook(){
        this.setTitleToMyShoppingBook();
        this.paginatedPane.clear();

        Trade trade = new Trade(player.getName());
        List<Trade.Order> myList = trade.getList();
        player.sendMessage("my shopping list: " + myList.toString());

        List<MyPane> myPanes = new LinkedList<>();

        int max_idx = myList.toArray().length;
        MyPane nextPage = new MyPane(8,5,1,1,Material.ACACIA_BUTTON,"다음 페이지로");
        MyPane backPage = new MyPane(0,5,1,1,Material.ACACIA_BUTTON,"이전 페이지로");
        MyPane air      = new MyPane(7,5,1,1,Material.ACACIA_BOAT, "보이면 오류");
        MyPane tmp;
        air.setVisible(true);

        k = 3+n;
        m = this.getMyShoppingListAndGetSizeOfList()/45;
        int idx=0;
        for(int i=0;i<=m;i++){
            this.addUnderBarButtonToPage(k+i);
            for(int row=0;row<5;row++){
                if(idx==max_idx)
                    break;
                for(int col=0;col<9;col++){
                    if(idx==max_idx)
                        break;
                    tmp=new MyPane(col,row,1,1,(this.shoppingList.get(idx).material), this.shoppingList.get(idx), this.shoppingList.get(idx).toString());
                    tmp.setAmount(this.MyShoppingList.get(idx).amount);
                    myPanes.add(tmp);
                    paginatedPane.addPane(k+i, tmp);
                    paginatedPane.addPane(k+i, nextPage);
                    paginatedPane.addPane(k+i, backPage);
                    idx++;
                }
            }
        }
;
        this.myShoppingBookClickEvent(myPanes, nextPage, backPage);
        this.paginatedPane.setPage(k);
        this.update(player, econ);
    }

    private int getShoppingListAndGetSizeOfList(){
        trade.setOrderListToAll();
        this.shoppingList=trade.getList();
        return this.shoppingList.size();
    }

    private int getMyShoppingListAndGetSizeOfList(){
        trade=new Trade(player.getName());
//        trade.setOrderListToAll();
        this.MyShoppingList=trade.getList();
        return this.shoppingList.size();
    }
    
    /** 각 창에서 사용하는 언더바 버튼클릭이벤트 */
    private void underBarButtonClickEvent(){
        goToPallete.setOnClick(
                inventoryClickEvent -> {
                    this.player.sendMessage("아이템선택 창으로 이동합니다.");
//                    paginatedPane.setPage();
                    this.setInvisibleBooks(0);

                    this.paginatedPane.clear();
                    this.setGUIToPallete();
                }
        );

        goToAskingOrder.setOnClick(
                inventoryClickEvent -> {
                    this.player.sendMessage("거래요청 창으로 이동합니다.");
                    this.setInvisibleBooks(1);

                    this.paginatedPane.clear();
                    this.setGUIToAskingOrder();
                    this.update(player, econ);
                }
        );

        goToOrderBook.setOnClick(
                inventoryClickEvent -> {
                    this.player.sendMessage("호가창으로 이동합니다.");
                    this.setInvisibleBooks(2);
//                    paginatedPane.setPage(1);
                    this.paginatedPane.clear();
                    this.setGUIToOrderBook();
                    this.update(player, econ);
                }
        );

        goToShoppingBook.setOnClick(
                inventoryClickEvent -> {
                    this.player.sendMessage("거래요청 리스트로 이동합니다");
                    this.setInvisibleBooks(3);

                    this.paginatedPane.clear();
                    this.setGUIToShoppingBook();
                }
        );

        goToMyShoppingBook.setOnClick(
                inventoryClickEvent -> {
                    this.player.sendMessage("내 거래요청 리스트로 이동합니다");
                    this.setInvisibleBooks(4);

                    this.paginatedPane.clear();
                    this.setGUIToMyShoppingBook();
                }
        );
    }

    /** 각 창에서 사용하는 언더바 버튼추가하기 */
    private void addUnderBarButtonToPage(int page){
        paginatedPane.addPane(page, goToPallete);
        paginatedPane.addPane(page, goToAskingOrder);
        paginatedPane.addPane(page, goToOrderBook);
        paginatedPane.addPane(page, goToShoppingBook);
        paginatedPane.addPane(page, goToMyShoppingBook);
    }

    /** 거래요청 창 클릭 이벤트 */
    private void askingOrderClickEvent(
            MyLabel confirm1, MyLabel confirm2_blue, MyLabel confirm2_gray,
            MyLabel incrementItem10, MyLabel incrementItem1,
            MyLabel decrementItem10, MyLabel decrementItem1,
            PlusMinusButton incrementMoney, PlusMinusButton decrementMoney
    ) {
        this.underBarButtonClickEvent();
        confirm1.setOnClick(
                    inventoryClickEvent -> {
                        boolean isPlus=getIsPlus();
                        String sellOrBuy=(isPlus)?" 판매":" 구매";
                        double price = getMoney(isPlus);

                        int amount = getAmount();
//                        int amount = 64;
                        double pricePerAmount = price/amount;
                        double maxMoney = econ.getBalance(player);
                        boolean money_is_not_enough = (maxMoney<price);


                        String message="거래요청 확정하시겠습니까?\n거래품목은"+
                                        material.toString()+
                                        " 거래수량은 "+
                                        Integer.toString(amount)+
                                        "개, 가격은 "+Double.toString(price) +
                                        "$, (개당 "+ Double.toString(pricePerAmount) +
                                        "$), 거래 유형은"+sellOrBuy+"입니다.\n";

                        player.sendMessage(message);
//
                        confirm2_gray.setVisible(false);
                        confirm2_gray.setPriority(Pane.Priority.LOW);

                        confirm2_blue.setVisible(true);
                        confirm2_blue.setPriority(Pane.Priority.HIGH);
                        this.update(player, econ);
                    }
        );
        
        confirm2_blue.setOnClick(
                inventoryClickEvent -> {

                    Trade trade_s = new Trade(econ);
                    boolean isPlus=sign.getText().equals("+");
                    String sellOrBuy=(isPlus)?" 판매":" 구매";
                    double price = getMoney(isPlus);
                    int amount = Integer.parseInt(amountItem10.getText());

                    boolean money_is_enough_so_well_withdrawed;// = setPlayersMoneyWhenTrade(price, isPlus);;
                    boolean item_is_enough_so_well_subtracted;// = setPlayersInventoryWhenTrade(amount);
                    if(isPlus){
                        money_is_enough_so_well_withdrawed=false;
                        item_is_enough_so_well_subtracted=trade_s.subPlayersInventoryWhenTrade(player, material, amount);
                    }else{
                        money_is_enough_so_well_withdrawed=trade_s.setPlayersMoneyWhenTrade(player,  price, isPlus);
                        item_is_enough_so_well_subtracted=false;
                    }

                    double pricePerAmount = price/amount;
                    Trade nullTrade = new Trade();
                    String message = isPlus?"아이템이 부족합니다":"돈이 부족합니다.";

                    if(nullTrade.getAvailOrderNumberPerUser(player.getName())>220){
                        player.sendMessage("현재 거래 개수 제한이 220개입니다.추후에 늘릴게요");
                    }

                    else {
                        if(isPlus){//판매
                            if(item_is_enough_so_well_subtracted) {
                                Trade trade = new Trade(price, amount, player.getName(), material, false);
                                player.sendMessage("거래요청 확정됐습니다.");
                            }
                            else {
                                player.sendMessage(message);
                            }
                        }else{  //구매
                            if(money_is_enough_so_well_withdrawed){
                                Trade trade = new Trade(price, amount, player.getName(), material, false);
                                player.sendMessage("거래요청 확정됐습니다.");
                            }else{
                                player.sendMessage(message);
                            }
                        }

                    }

                    confirm2_gray.setVisible(true);
                    confirm2_gray.setPriority(Pane.Priority.HIGH);
                    confirm2_blue.setVisible(false);
                    confirm2_blue.setPriority(Pane.Priority.LOW);
                    this.update(player, econ);
                }
        );
        incrementItem10.setOnClick(
                inventoryClickEvent -> {
                    int amount = Integer.parseInt(amountItem10.getText());
                    amount = (amount<=(64-10))?(amount+10):64;
                    amountItem10.setText(
                            Integer.toString(amount)
                    );
                    this.update(player, econ);
                }
        );
        incrementItem1.setOnClick(
                inventoryClickEvent -> {
                    int amount = Integer.parseInt(amountItem10.getText());
                    amount = (amount<=(64-1))?(amount+1):64;
                    amountItem10.setText(
                            Integer.toString(amount)
                    );
                    this.update(player, econ);
                }
        );
        decrementItem10.setOnClick(
                inventoryClickEvent -> {
                    int amount = Integer.parseInt(amountItem10.getText());
                    amount = (amount>=(0+10))?(amount-10):0;
                    amountItem10.setText(
                            Integer.toString(amount)
                    );
                    this.update(player, econ);
                }
        );
        decrementItem1.setOnClick(
                inventoryClickEvent -> {
                    int amount = Integer.parseInt(amountItem10.getText());
                    amount = (amount>=0+1)?(amount-1):0;
                    amountItem10.setText(
                            Integer.toString(amount)
                    );
                    this.update(player, econ);
                }
        );
        this.setPlusMinusButton(incrementMoney, true);
        this.setPlusMinusButton(decrementMoney, false);
    }

    /** 호가 창 클릭 이벤트 */
    private void orderBookClickEvent(MyPane itemPane){
        this.underBarButtonClickEvent();
        itemPane.setOnClick(
                inventoryClickEvent -> {
                    preventTakeItem(inventoryClickEvent,this);
                }
        );
    }

    /** 요청리스트 창 클릭 이벤트 */
    private void shoppingBookClickEvent(List<MyPane> panesOnShoppingBook, MyPane nextPage, MyPane backPage){

        for(MyPane pane : panesOnShoppingBook){
            if(pane.order.is_selling){
                pane.setOnClick(
                        inventoryClickEvent -> {
                            this.clickEventForSellOrder(pane,inventoryClickEvent);
                        }
                );
            }else{
                pane.setOnClick(
                        inventoryClickEvent -> {
                            this.clickEventForBuyOrder(pane,inventoryClickEvent);
                        }
                );
            }
        }
        this.movePageClickEvent(nextPage, backPage, false);
        this.underBarButtonClickEvent();
    }


    /** 내 요청리스트 창 클릭 이벤트 */
    private void myShoppingBookClickEvent(List<MyPane> panesOnMyShoppingBook, MyPane nextPage, MyPane backPage){

//        player.sendMessage("tttttt");
        for(MyPane pane : panesOnMyShoppingBook){
            pane.setOnClick(
                    inventoryClickEvent -> {
                        String inf=pane.order.toString();
                        String c = inventoryClickEvent.getClick().toString();
                        Trade itrade= new Trade();
                        String message;

                        if(c.equals("LEFT")){
                            message="우클릭을 누르면 거래가 취소됩니다.\n"+inf;
//                            player.sendMessage(message);
                            preventTakeItem(inventoryClickEvent, this);
                        }else if(c.equals("RIGHT")){
                            message="§d§l거래취소됐습니다.§r 좌클릭을 누르면 거래정보만 출력합니다.\n"+""+inf;
//                            player.sendMessage(message);
                            itrade.setOrderCanceled(pane.order.order_id);
                            pane.setVisible(false);
                            pane.clear();
                            this.update(player, econ);
                            addItemToInventoryWhenCancelTrade(pane.order);
                        }else{
                            message="우클릭은 거래취소, 좌클릭은 정보출력,\n";
                            preventTakeItem(inventoryClickEvent, this);
                        }

                        this.player.sendMessage(message);
                    }
            );
        }
        this.movePageClickEvent(nextPage, backPage, true);
        this.underBarButtonClickEvent();

    }

    private void movePageClickEvent(MyPane nextPage, MyPane backPage, boolean isMyShopping) {
        nextPage.setOnClick(
                inventoryClickEvent -> {
                    int nowPage=paginatedPane.getPage();
                    int maxPage=isMyShopping?(m+k):(n+2);

                    int nowPageIndic=nowPage-(isMyShopping?k-1:1);
                    int maxPageIndic=maxPage-(isMyShopping?k-1:1);
                    int nextPageNum=(nowPage>=maxPage)?maxPage:nowPage+1;

                    int nextPageIndic=nextPageNum-(isMyShopping?k-1:1);
                    String indic = "("+nextPageIndic+"/"+maxPageIndic+")";
                    this.setTitle((isMyShopping?"내 요청리스트":"요청리스트")+indic);

                    this.paginatedPane.setPage(nextPageNum);
                    this.update(player, econ);
                }
        );
        backPage.setOnClick(
                inventoryClickEvent -> {
                    int nowPage = paginatedPane.getPage();
                    int minPage = isMyShopping?k:2;
                    int maxPage=isMyShopping?(m+k):(n+2);

                    int nowPageIndic=nowPage-(isMyShopping?k-1:1);
                    int maxPageIndic=maxPage-(isMyShopping?k-1:1);

                    int nextPageNum = nowPage<=minPage?minPage:nowPage-1;

                    int nextPageIndic=nextPageNum-(isMyShopping?k-1:1);
                    String indic = "("+nextPageIndic+"/"+maxPageIndic+")";
                    this.setTitle((isMyShopping?"내 요청리스트":"요청리스트")+indic);

                    this.paginatedPane.setPage(nextPageNum);
                    this.update(player, econ);
                }
        );
    }
    private void clickEventForSellOrder(MyPane i, InventoryClickEvent inventoryClickEvent){
        String info = i.order.toString();
        String click = inventoryClickEvent.getClick().toString();
        String message;
        Trade i_trade = new Trade((CommandSender) player, i.order, econ);

        if(click.equals("LEFT")){
            message="거래요청에 응하시겠습니까? 우클릭을 누르면 거래완료됩니다."+info;
            preventTakeItem(inventoryClickEvent, this);
        }else if(click.equals("RIGHT")){
            if(i_trade.enoughMoney(player) ){
                addItemToInventoryWhenCancelTrade(i.order);
                message = "거래가 완료됐습니다. 환불은 안됨 " + info;
                i.setVisible(false);
                i.clear();
            }else{
                message = "돈이 모자란데요?";
                preventTakeItem(inventoryClickEvent, this);
            }
            this.update(player, econ);
        }else{
            message="우클릭은 거래승낙, 좌클릭은 정보출력";
            preventTakeItem(inventoryClickEvent, this);
        }
        player.sendMessage(message);
    }
    private void clickEventForBuyOrder(MyPane i, InventoryClickEvent inventoryClickEvent){
        player.sendMessage("testttttttt");
        String info = i.order.toString();
        String click = inventoryClickEvent.getClick().toString();
        String message;
        Trade i_trade = new Trade((CommandSender)player, i.order, econ);

        if(click.equals("LEFT")){
            message="거래요청에 응하시겠습니까? 우클릭을 누르면 거래완료됩니다."+info;
            preventTakeItem(inventoryClickEvent, this);
        }else if(click.equals("RIGHT")){
            if(i_trade.enoughItemSoPaid(player)){
                message = "거래가 완료됐습니다. 환불은 안됨 " + info;
                i.setVisible(false);
                i.clear();
            }else{
                message = "아이템이 모자란데요?";
                preventTakeItem(inventoryClickEvent, this);
            }
            this.update(player, econ);
        }else{
            message="우클릭은 거래승낙, 좌클릭은 정보출력";
            preventTakeItem(inventoryClickEvent, this);
        }

        player.sendMessage(message);
    }

    private void setInvisibleBooks(int i){
        goToPallete.setVisible(i!=0);
        goToAskingOrder.setVisible(i!=1);
        goToOrderBook.setVisible(i!=2);
        goToShoppingBook.setVisible(i!=3);
        goToMyShoppingBook.setVisible(i!=4);
    }
    private void addPlusMinusButtonToPaginatedPane(int page, PlusMinusButton xxcrement){
        for(MyLabel i : xxcrement.button){
            paginatedPane.addPane(page, i);
        }
    }
    private void setPlusMinusButton(PlusMinusButton xxcrement, boolean isIncrement){
        int[] delta={1000000,100000,10000,1000,100,10,1};//자리수별 증감

        for(int i=0;i<7;i++){
            int finalI = i;
            xxcrement.button[i].setOnClick(event->{
                    boolean isPlus=getIsPlus();
                    int number=getMoney(isPlus);

                    number=number+((isIncrement)?delta[finalI]:-delta[finalI]);
                    boolean isGreaterThanZero=(number>=0);

                    if(!isGreaterThanZero) {
                        this.sign.setText("-");
                        this.sell.setText("B");
                        this.sell.setShowName("구매요청");
                    }
                    else {
                        this.sign.setText("+");
                        this.sell.setText("S");
                        this.sell.setShowName("판매요청");
                    }

                    int numWillBeString=(isGreaterThanZero)?(number):(-number);
                    String stringNumber=Integer.toString(numWillBeString);
                    int len= 7-stringNumber.length();
                    String zero = "";
                    for(int j=0;j<len;j++)
                        zero=zero+"0";

                    this.moneyLabel.setText(zero+stringNumber);
                    this.update(player, econ);
                }
            );
        }
    }
    private boolean getIsPlus(){
        return sign.getText().equals("+");
    }
    private int getMoney(@NotNull boolean isPlus){
        String moneyTest=moneyLabel.getText();
        return (Integer.parseInt(moneyTest))*(isPlus?1:-1);
    }
    private int getAmount(){
        return Integer.parseInt(this.amountItem10.getText());
    }
    public void main(Material material){
        setGUIToAskingOrder(material);
        this.show(player);
    }
    public static void preventTakeItem(InventoryClickEvent inventoryClickEvent, MyChestGui gui) {
        inventoryClickEvent.getWhoClicked().closeInventory();
        gui.update();
        gui.show(inventoryClickEvent.getWhoClicked());
    }
    public static class MyPane extends OutlinePane{
//        @NotNull
        public Trade.Order  order;
        private ItemStack item;

//        private MyPane(int x, int y, int length, int height, Material material, Trade.Order order, ChestGui gui, String showname){
//            super(x,y,length, height);
//            item = new ItemStack(material);
//            ItemMeta itemMeta = item.getItemMeta();
//            itemMeta.setDisplayName(showname);
//            item.setItemMeta(itemMeta);
////            itemMeta.setLocalizedName("test");
//            this.addItem(new GuiItem(item));
//            this.order=order;
//            gui.addPane(this);
//            this.setVisible(true);
//        }
//        private MyPane(int x, int y, int length, int height, Material material, Trade.Order order, String showname){
//            super(x,y,length, height);
//            item = new ItemStack(material);
//            ItemMeta itemMeta = item.getItemMeta();
//            itemMeta.setDisplayName(showname);
//            item.setItemMeta(itemMeta);
////            itemMeta.setLocalizedName("test");
//            this.addItem(new GuiItem(item));
//            this.order=order;
//            this.setVisible(true);
//        }
//        public MyPane(int x, int y, int length, int height, Material material, Trade.Order order, ChestGui gui){
//            this(x,y,length,height,material,order,gui,material.name());
//        }

        public MyPane(int x, int y, int length, int height, Material material, String showName){
            super(x,y,length, height);
            item = new ItemStack(material);
            ItemMeta itemMeta = item.getItemMeta();
            itemMeta.setDisplayName(showName);
            item.setItemMeta(itemMeta);
//            itemMeta.setLocalizedName("test");
            this.addItem(new GuiItem(item));
//            gui.addPane(this);
            this.setVisible(true);
        }
        private MyPane(int x, int y, int length, int height, Material material, Trade.Order order, String showname){
            super(x,y,length, height);
            item = new ItemStack(material);
            ItemMeta itemMeta = item.getItemMeta();
            itemMeta.setDisplayName(showname);
            item.setItemMeta(itemMeta);
//            itemMeta.setLocalizedName("test");
            this.addItem(new GuiItem(item));
            this.order=order;
            this.setVisible(true);
        }

        public void setAmount(int amount){
            this.item.setAmount(amount);
        }
        private void setOrder(Trade.@NotNull Order order){
            this.order=order;
        }

//        private void setOrder(Trade.@NotNull Order order){
//            this.order=order;
//        }

        public void setShowname(String showname){
//            this.clear();

            ItemMeta itemMeta = item.getItemMeta();
            itemMeta.setDisplayName(showname);
            item.setItemMeta(itemMeta);
//            this.addItem(new GuiItem(item));
        }
        public String getShowname(){
            return item.getItemMeta().getDisplayName();
        }

        private void preventTakeItemOnly(MyChestGui gui){
            this.setOnClick(
                    inventoryClickEvent -> {
                        preventTakeItem(inventoryClickEvent, gui);
                    }
            );
        }

    }
    private class MyLabel extends MyLabel_v2{
        private MyLabel(int x, int y, int length, int height, Font font, String text, String showName){
            super(x,y,length,height,font);
            super.setText(text, showName);
//            gui.addPane(this);
        }
        private MyLabel(int x, int y, int length, int height,Font font, String text, ChestGui gui, String showName){
            super(x,y,length,height,font);
            super.setText(text, showName);
            gui.addPane(this);
        }
        private void setShowName(String showName){
            super.setText(this.getText(),showName);
        }
    }
    private class PlusMinusButton{
        public MyLabel[] button = new MyLabel[7];
        private String[] name = {
            "1의    자리(음수면 구매요청)",
            "10의   자리(음수면 구매요청)",
            "100의  자리(음수면 구매요청)",
            "1000의 자리(음수면 구매요청)",
            "10000의 자리(음수면 구매요청)",
            "100000의 자리(음수면 구매요청)",
            "1000000의 자리(음수면 구매요청)"
        };

        PlusMinusButton(boolean isPlus){
            Font font=(isPlus)?(Font.RED):(Font.GREEN);
            String sign=(isPlus)?"+":"-";
            int y=(isPlus)?(1):(2);
            for(int i=0;i<7;i++){
                button[i] = new MyLabel(i+1, y,1,1,font,sign,name[i]);
            }

        }
    }
    private boolean addItemToInventoryWhenCancelTrade(Trade.Order order){
        try{
            if(order.is_selling){
                PlayerInventory inventory = (player).getInventory();
                ItemStack itemStack = new ItemStack(order.material);
                itemStack.setAmount(order.amount);
                inventory.addItem(itemStack);
            }else{
                double price=abs(order.price);
                econ.depositPlayer(player, price);
            }
            return true;
        }
        catch (Exception e){
            player.sendMessage(e.toString());
            return false;
        }
    }
    private class AskPrice{
        List<Trade.Order> orderList;
        List<Trade.Order> orderList_sellOnly;
        List<Trade.Order> orderList_buyOnly;
        List<MyPane> orderPanes_sellOnly = new LinkedList<>();
        List<MyPane> orderPanes_buyOnly = new LinkedList<>();
        List<MyPane> blackglass = new LinkedList<>();
        PageGUI gui;
        private final Integer[] sellXArray = {3,2,1, 0,0,0,1, 2,3,3, 3,2,1,0};
        private final Integer[] sellYArray = {0,0,0, 0,1,2,2, 2,2,3, 4,4,4,4};
        private final Integer[] buyXArray = {5,6,7, 8,8,8,7, 6,5,5, 5,6,7,8};
        private final Integer[] buyYArray = {0,0,0, 0,1,2,2, 2,2,3, 4,4,4,4};
        /** 호가창 */
        AskPrice(PageGUI gui){
            this.gui=gui;
            Trade trade = new Trade(material, (Player) player);
            orderList = trade.getList();

            this.setSellBuyOrder();
            this.sortByPrice();
            player.sendMessage("orderList_sellOnly: "+orderList_sellOnly.toString());
            player.sendMessage("orderList_buyOnly: "+orderList_buyOnly.toString());
            MyPane tmp1, tmp2;
            for(int i=0;i<14;i++){
                tmp1=new MyPane(sellXArray[i], sellYArray[i], 1,1, material,   "testdef");
                tmp2=new MyPane(buyXArray[i], buyYArray[i], 1,1, material,   "testabc");
                orderPanes_sellOnly.add(tmp1);
                orderPanes_buyOnly.add(tmp2);
                paginatedPane.addPane(1,tmp1);
                paginatedPane.addPane(1,tmp2);
            }

            for(int i=0;i<14;i++){
                if(i<orderList_sellOnly.size()){
                    orderPanes_sellOnly.get(i).setOrder(orderList_sellOnly.get(i));
                    orderPanes_sellOnly.get(i).setShowname(orderList_sellOnly.get(i).toString());
                }else{
                    orderPanes_sellOnly.get(i).setVisible(false);
                }

                if(i<orderList_buyOnly.size()){
                    orderPanes_buyOnly.get(i).setOrder(orderList_buyOnly.get(i));
                    orderPanes_buyOnly.get(i).setShowname(orderList_buyOnly.get(i).toString());
                }else{
                    orderPanes_buyOnly.get(i).setVisible(false);
                }
            }

//            askOrderBookGui.update(player, econ);
            //
            for(MyPane i : orderPanes_sellOnly){
                i.setOnClick(
                        inventoryClickEvent -> {
                            String info = i.order.toString();
                            String click = inventoryClickEvent.getClick().toString();
                            String message;
                            Trade i_trade = new Trade(player, i.order, econ);

                            if(click.equals("LEFT")){
                                message="거래요청에 응하시겠습니까? 우클릭을 누르면 거래완료됩니다."+info;
                                preventTakeItem(inventoryClickEvent, gui);
                            }else if(click.equals("RIGHT")){
                                if(i_trade.enoughMoney(player)) {
                                    addItemToInventoryWhenCancelTrade(i.order);

                                    message = "거래가 완료됐습니다. 환불은 안됨 " + info;
                                    i.setVisible(false);
                                    i.clear();
                                }else{
                                    message = "돈이 모자란데요?";
                                    preventTakeItem(inventoryClickEvent, gui);
                                }
                                gui.update(player, econ);
                            }else{
                                message="우클릭은 거래승낙, 좌클릭은 정보출력";
                                preventTakeItem(inventoryClickEvent, gui);
                            }
                            player.sendMessage(message);
                        }
                );
            }
//            askOrderBookGui.update(player, econ);
            for(MyPane i : orderPanes_buyOnly){
                i.setOnClick(
                        inventoryClickEvent -> {
                            String info = i.order.toString();
                            String click = inventoryClickEvent.getClick().toString();
                            String message;
                            Trade i_trade = new Trade(player, i.order, econ);

                            if(click.equals("LEFT")){
                                message="거래요청에 응하시겠습니까? 우클릭을 누르면 거래완료됩니다."+info;
                                preventTakeItem(inventoryClickEvent, gui);
                            }else if(click.equals("RIGHT")){
                                if(i_trade.enoughItemSoPaid(player)) {
                                    message = "거래가 완료됐습니다. 환불은 안됨 " + info;
                                    i.setVisible(false);
                                    i.clear();
                                }else{
                                    message = "아이템이 모자란데요?";
                                    preventTakeItem(inventoryClickEvent, gui);
                                }
//                                askOrderBookGui.update(player, econ);
                            }else{
                                message="우클릭은 거래승낙, 좌클릭은 정보출력";
                                preventTakeItem(inventoryClickEvent, gui);
                            }

                            player.sendMessage(message);
                        }
                );
            }

//            ItemStack itemStack = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
            for(int i=0; i<6;i++){
                tmp1=new MyPane(4,i,1,1,Material.BLACK_STAINED_GLASS_PANE,"칸막이");
                blackglass.add(tmp1);
                paginatedPane.addPane(1,tmp1);
            }

            for(MyPane i : blackglass){
                i.setOnClick(
                        inventoryClickEvent -> {
                            preventTakeItem(inventoryClickEvent, gui);
                        }
                );
            }
//            askOrderBookGui.update(player, econ);
            gui.update(player, econ);
        }

        public void filteringByMaterial(){
            orderList.removeIf(i -> !(i.material.equals(material)));
        }

        private void setSellBuyOrder(){
            orderList_sellOnly=new LinkedList<>(orderList);
            orderList_sellOnly.removeIf(i -> !(i.is_selling));
            orderList_buyOnly=new LinkedList<>(orderList);
            orderList_buyOnly.removeIf(i -> (i.is_selling));
        }

        private void sortByPrice(){
            sortOrdersByPrice();
        }

        public void sortOrdersByPrice() {

            if(orderList_sellOnly.size()!=0)
                AskPrice.QuickSort.quickSort(orderList_sellOnly, 0, orderList_sellOnly.size() - 1);

            if(orderList_buyOnly.size()!=0)
                AskPrice.QuickSort.quickSort(orderList_buyOnly, 0, orderList_buyOnly.size() - 1);
        }
        private class QuickSort {
            public static void quickSort(List<Trade.Order> orders, int left, int right) {
                int i = left, j = right;
                double pivot = orders.get((left + right) / 2).pricePerAmount;
                while (i <= j) {
                    while (orders.get(i).pricePerAmount < pivot) {
                        i++;
                    }
                    while (orders.get(j).pricePerAmount > pivot) {
                        j--;
                    }
                    if (i <= j) {
                        swap(orders, i, j);
                        i++;
                        j--;
                    }
                }
                if (left < j) {
                    quickSort(orders, left, j);
                }
                if (i < right) {
                    quickSort(orders, i, right);
                }
            }

            private static void swap(List<Trade.Order> orders, int i, int j) {
                Trade.Order temp = orders.get(i);
                orders.set(i, orders.get(j));
                orders.set(j, temp);
            }
        }
    }

}

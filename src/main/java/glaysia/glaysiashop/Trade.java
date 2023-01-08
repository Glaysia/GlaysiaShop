package glaysia.glaysiashop;

import org.bukkit.Material;

import java.util.Date;

public class Trade {
        private static int order_id=0;          //거래번호
    public class Order{                         //주문정보를 담고있는 클래스
        Order(int order_id, Date date, double price, int amount, String trader, Material material){
            this.order_id=order_id;
            this.date=date;
            this.price=price;
            this.amount=amount;
            this.trader=trader;
            this.material=material;
            this.is_selling=(price>=0);
        }
        private final int order_id;            //거래번호 불변
        private final Date date;                 //거래시간 불변
        private final double price;               //가격 음수면 구매요청, 양수면 판매요청
        private final int amount;                   //개수
        private final String trader;             //주문자 이름
        private final Material material;         //아이템
        private final boolean is_selling;             //true면 판매요청, false면 구매요청
        
        private boolean is_canceled;            //취소됐는지 여부
        private boolean is_completed;           //거래완료됐는지 여부
        private boolean is_there_error;         //각 단계에서, 플레이어에게 아이템, 금액 등이 제대로 정산됐는지 여부

        //get, set 메소드 구현해야함

    }
    private boolean is_saved;


    Trade(double price, int amount, String trader, Material material, boolean was_there_error){
        Trade.order_id++;                //거래할 때마다 증가. static변수를 사용.

        Order order = new Order(
                Trade.order_id,
                (new Date()),
                price,
                amount,
                trader,
                material
        );

        order.is_canceled=false;
        order.is_completed=false;
        order.is_there_error=was_there_error;   //거래 시작했을 때 아이템, 금액이 정산됐는지 여부
        is_saved=saveToDB();
    }

    private boolean saveToDB(){
//        return someFunction(order); //DB에 저장에 성공했다면 true를 반환

        return false;
    }

    public boolean isSaved(){
        return is_saved;
    }
    private Order ReadFromDB(int order_id){
//        return someFunction(order_id);
        Order exampleOrder = new Order(
                1,
                (new Date(1673122855911L)),
                -300.0,
                64,
                "Glaysia",
                Material.ACACIA_LOG
        );
        exampleOrder.is_canceled=false;
        exampleOrder.is_completed=false;
        exampleOrder.is_there_error=false;//예시


        return exampleOrder;
    }
    
    Trade(int order_id){
        Order order = null;
        order=ReadFromDB(order_id);       //주문번호를 통해 DB에 접근하여 데이터를 꺼내옴
    }



}

package glaysia.glaysiashop;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.imageio.spi.ImageInputStreamSpi;

import static glaysia.glaysiashop.GlaysiaShop.log;

public class Transfer_v2 implements CommandExecutor {
    private DataIO dataIO=null;
    private Economy econ=null;
    private String errorMessage="다이아->돈 환전: 다이아 들고 명령\n돈->다이아 환전: 빈손으로 명령 ";
    private final double RATIO_OF_DIA=1000;
    public Transfer_v2(Economy econ){ this.econ=econ; }


    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (sender instanceof Player) { //명령어를 사용자가 입력했으면
            Player player = (Player) sender;
            dataIO=new DataIO(player);

            int argument;
            try{
                argument=Integer.parseInt((args.length==1)?args[0]:"0");
            } catch (NumberFormatException e){
                sender.sendMessage("정수만 입력하셔야지");
                return false;
            }

            String itemInMainHand = player.getInventory().getItemInMainHand().translationKey();
            int mainHandNumber=player.getInventory().getItemInMainHand().getAmount();
            final boolean isHandEmpty = itemInMainHand.equals("block.minecraft.air");
            final boolean diamondToMoney = itemInMainHand.equals("item.minecraft.diamond");
            final boolean moneyToDiamond = (isHandEmpty)&&(args.length==1);

            double maxMoneyOfPlayer=econ.getBalance(player);
            int amountOfTrans = moneyToDiamond?argument:mainHandNumber;
            amountOfTrans=((args.length==1))?argument:mainHandNumber;
            if(amountOfTrans>64){
                sender.sendMessage("1회 최대 64개에요");
                return false;
            }
            double tranferringMoney=amountOfTrans*RATIO_OF_DIA;

            int beforeDia = dataIO.getDiamondFromYml();
            double beforeMoney = dataIO.getMoneyFromYml();

            //국고의 돈을 다이아몬드로 환전
            if(moneyToDiamond){
                if(maxMoneyOfPlayer<tranferringMoney) {
                    sender.sendMessage("모자란데요?");
                    return false;
                }
                SetMoney.subMoney(player, econ, tranferringMoney);
                boolean success=dataIO.writeBankMoneyToYml((beforeMoney+tranferringMoney), (beforeDia-amountOfTrans));
                log.info("debug at trans after"+Integer.toString(beforeDia-amountOfTrans)+" 길이: "+Integer.toString(args.length));

                String message=Double.toString(tranferringMoney) + "$를 다이아" + Integer.toString(amountOfTrans) + "개로 환전하였습니다.";
                message=success?message:"환전내역 저장 실패";
                Material dia=Material.DIAMOND;
                ItemStack itemStack=new ItemStack(dia);
                itemStack.setAmount(amountOfTrans);
                player.getInventory().addItem(itemStack);

                sender.sendMessage(message);
                return success;
//                return true;
            }
            else if(diamondToMoney){
            //다이아를 국고의 돈으로 환전
                if(mainHandNumber<amountOfTrans){
                    sender.sendMessage("모자란데요?");
                    return false;
                }
//                econ.depositPlayer(player, tranferringMoney);
                SetMoney.addMoney(player, econ, tranferringMoney);

                boolean success=dataIO.writeBankMoneyToYml((beforeMoney-tranferringMoney), (beforeDia+amountOfTrans));

                player.getInventory().getItemInMainHand().setAmount(mainHandNumber-amountOfTrans);
                String message="다이아몬드 " + Integer.toString(amountOfTrans) + "개를 " + Double.toString(tranferringMoney) + "$로 환전하였습니다.";
                message=success?message:"환전내역 저장 실패";
                sender.sendMessage(message);
                return true;
            }else{
                sender.sendMessage("다이아->돈 환전: 다이아 들고 명령어 사용\n돈->다이아 환전: 빈손으로 명령어 사용");
                return false;
            }
        }
        else if (sender instanceof ConsoleCommandSender) {
            //콘솔창에서 사용한 경우
            sender.sendMessage("콘솔에서는 사용할 수 없습니다.");
            return false;
        }
        return false;
    }
}

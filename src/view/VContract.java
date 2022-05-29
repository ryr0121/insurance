package Practice.InsuranceCompany.Design.src.view;

import Practice.InsuranceCompany.Design.src.contract.Contract;
import Practice.InsuranceCompany.Design.src.contract.ContractListImpl;
import Practice.InsuranceCompany.Design.src.customer.Customer;
import Practice.InsuranceCompany.Design.src.customer.CustomerListImpl;
import Practice.InsuranceCompany.Design.src.etcEnum.UnderwritingStatus;
import Practice.InsuranceCompany.Design.src.insurance.InsuranceListImpl;
import Practice.InsuranceCompany.Design.src.subscription.Subscription;
import Practice.InsuranceCompany.Design.src.subscription.SubscriptionListImpl;

import java.util.Scanner;

public class VContract {

    private Scanner scn;
    private ContractListImpl contractList;
    private SubscriptionListImpl subscriptionList;
    private CustomerListImpl customerList;
    private InsuranceListImpl insuranceList;

    public VContract(Scanner scn, ContractListImpl contractList, SubscriptionListImpl subscriptionList, CustomerListImpl customerList, InsuranceListImpl insuranceList){
        this.scn=scn;
        this.contractList=contractList;
        this.subscriptionList=subscriptionList;
        this.customerList=customerList;
        this.insuranceList=insuranceList;
    }

    public void show() {
        while (true) {
            System.out.println("(1)계약 조회 (2)계약 체결 여부 설정 및 안내 (3)계약 유지 활동 대상 조회 및 수정");
            System.out.println("(b)뒤로가기");
            String input = scn.next();
            switch (input) {
                case "1" : inquireAllContract(); break;
                case "2" : setContractStatus(); break;
                case "3" : setMaintenanceActivityDate(); break;
                case "b" : return;
                default : System.out.println("잘못 입력하셨습니다. 다시 입력해주세요.");
            }
        }
    }


    private boolean inquireAllContract(){
        System.out.println("----------------------------계약 전체 목록----------------------------");
        System.out.println("(계약ID) (고객ID) (보험ID) (월보험료) (가입일자) (가입기간) (계약유지활동일자)");
        for(Contract contract : this.contractList.getAllList())
            System.out.println(contract.getContractInfo());
        return true;
    }

    private void setContractStatus() {
        System.out.println("---------------인수심사 결과 조회---------------");
        System.out.print("청약서 ID : ");
        String subscriptionID = scn.next();

        Subscription subscription= this.subscriptionList.getBySubscriptionID(subscriptionID);
        String cusID = subscription.getCustomerID();
        String cusName=this.customerList.getByCustomerId(cusID).getCustomerName();
        String insuranceID =subscription.getInsuranceID();
        String insuranceName=this.insuranceList.get(insuranceID).getInsuranceName();
        String date = subscription.getDateCreated();
        UnderwritingStatus status = subscription.getUnderwritingStatus();
        String insuranceAgentID=subscription.getInsuranceAgentID();

        System.out.println("-----------------"+cusName+"님의 청약서-----------------");
        System.out.println("(가입자명) (보험명) (작성일자) (인수심사상태) (담당 설계사 ID)");
        System.out.println(cusName+" "+insuranceName+" "+date+" "+status.name()+" "+insuranceAgentID);
        if(status==UnderwritingStatus.applied||status==UnderwritingStatus.notApplied){
            System.out.println("아직 인수심사 결과가 없습니다."); return;
        }else if(status==UnderwritingStatus.completed) {
            System.out.println("이미 계약 체결 여부가 결정된 청약서입니다."); return;
        }
        System.out.println("---------------계약 체결 여부 결정---------------");
        System.out.println("(1) 계약 체결 완료 (2) 계약 체결 반려 (c) 나가기");
        String input = scn.next();
        switch (input) {
            case "1" :
                int period=subscription.getContractPeriod();
                int premium=subscription.getPremium();
                Contract contract=new Contract(cusID,insuranceID,period,premium,insuranceAgentID);
                this.contractList.add(contract);
                this.subscriptionList.updateUnderwritingStatus(subscription.getSubscriptionID(),UnderwritingStatus.completed);
                System.out.println("계약 체결 완료 처리되었습니다.");
                break;
            case "2" :
                this.subscriptionList.updateUnderwritingStatus(subscription.getSubscriptionID(),UnderwritingStatus.completed);
                System.out.println("계약 체결 반려 처리되었습니다.");
                break;
            case "c" : return;
        }
        announceContractStatus();
    }

    private void announceContractStatus() {
        System.out.println("------------계약 체결 결과 전송------------");
        System.out.println("계약 체결 결과를 전송하시겠습니까? (y)");
        String send = scn.next();
        if (send.equals("y")) System.out.println("보험 관심자에게 성공적으로 전송되었습니다.");
        else  System.out.println("전송에 실패했습니다.");
    }

    private boolean inquireMaintenanceContract(){
        System.out.println("------------------------계약 유지 활동 대상 목록------------------------");
        System.out.println("(계약ID) (보험명) (고객명) (생년월일) (가입일자) (가입기간) (계약유지활동일자)");
        for(Contract contract : this.contractList.getMaintenanceTargetList()){
            Customer customer = this.customerList.getByCustomerId(contract.getCustomerID());
            String insuranceID =contract.getInsuranceID();
            String insuranceName = this.insuranceList.get(insuranceID).getInsuranceName();
            String cusName = customer.getCustomerName();
            String birth = customer.getDateOfBirth();
            String joinDate = contract.getJoinDate();
            int period = contract.getContractPeriod();
            String maintenanceDate = contract.getActivityDate();
            System.out.println(insuranceID+" "+insuranceName+" "+cusName+" "+birth+" "+joinDate+" "+period+" "+maintenanceDate);
        }
        return true;
    }

    private void setMaintenanceActivityDate() {
        while (true) {
            inquireMaintenanceContract();
            System.out.println("------------계약 유지 활동 일자 수정------------");
            System.out.print("계약 ID : ");
            String contractID = scn.next();
            Contract contract = this.contractList.getByContractId(contractID);
            while (true) {
                System.out.println("(c)나가기");
                System.out.print("계약 유지 활동 일자 :");
                String activityDate = scn.next();
                if (activityDate.equals("c")) {
                    System.out.println("변경사항이 저장되지 않았습니다. 입력을 취소하시겠습니까?");
                    System.out.println("(y)예 (n)아니오");
                    String response = scn.next();
                    if (response.equals("y")) break;
                    else if (response.equals("n")) continue;
                }
                contract.setActivityDate(activityDate);
                System.out.println("계약유지활동의 변경된 정보가 저장되었습니다.");
                return;
            }
        }
    }
}
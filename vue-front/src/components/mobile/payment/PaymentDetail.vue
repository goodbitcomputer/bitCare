<template>
<div>
  <button @click="iamport">결제하기</button>
</div>
</template>

<script>
import axios from "axios";
const Swal = window.Swal;
const IMP = window.IMP;
//가맹점 식별코드
IMP.init('imp27148222');

export default {
  name: "MobilePaymentDetail",
  methods:{
    iamport(){
      IMP.request_pay({
        pg : 'kakaopay',
        pay_method : 'kakaopay',
        merchant_uid : 'merchant_' + new Date().getTime(),
        name : '비트케어' , //결제창에서 보여질 이름
        amount : 100, //실제 결제되는 가격
        buyer_name : '구매자이름',
        buyer_tel : '010-7399-2128',
      }, function(rsp) {
        console.log(rsp);
        // 결제검증
        axios.post('/mobile/payment_proc', {
          impUid: rsp.imp_uid ,
          patientId: 99,
        }).then((response) => {
          console.log(response.data.response);
          if (rsp.paid_amount == response.data.response.amount) {
            Swal.fire({
              icon: 'success',
              title: 'success',
              text: '결제 및 결제검증완료',
              showConfirmButton: false,
              timer: 1000
            }).then(() => {
              // this.$router.push('/mobile/home');
            })
          }else{
            Swal.fire({
              icon: 'error',
              title: 'error',
              text: '결제 실패',
              showConfirmButton: false,
              timer: 1000
            })
          }
        }).catch(function (error) {
          console.log(error);
        })



        var msg ="";
        if ( rsp.success ) {
          msg = '결제가 완료되었습니다.';
          msg += '고유ID : ' + rsp.imp_uid;
          msg += '상점 거래ID : ' + rsp.merchant_uid;
          msg += '결제 금액 : ' + rsp.paid_amount;
          msg += '카드 승인번호 : ' + rsp.apply_num;
        } else {
          msg = '결제에 실패하였습니다.';
          msg += '에러내용 : ' + rsp.error_msg;
        }
        alert(msg);
      });
    }
  },
}
</script>

<style scoped>

</style>
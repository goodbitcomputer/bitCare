<template>
  <!-- ======= Hero Section ======= -->
  <section id="mobile-payment">
    <div id="mobileDoctor-box" class="text-center text-lg-start" data-aos="fade-up">
      <div class="util" data-aos="fade-up">
        <div class="title">
          <span class="font-weight-bold">{{ this.patientData.name }}</span>
          <span>cn.{{ this.patientData.id }}</span>
          <span>{{ dateMsg(this.patientData.birth) }}</span>
          <span>{{ ageMsg(this.patientData.birth) }}세</span>
          <span>{{ this.patientData.gender }}</span>
        </div>
      </div>

      <div class="receipt-list-box" data-aos="fade-up" data-aos-delay="100">
        <div class="receipt-box border-box" v-for="(item) in receiptList"
             :key="item.id">
          <div>
            <div class=" title ellipsis-name">
              <span class="font-weight-bold">{{ dateMsg(item.entryDate) }}</span>
              <span>{{ item.visit }}</span>
              <span>{{ item.dept }}</span>
              <span>{{ item.name }}</span>
            </div>
            <div class="patient-info">
              <div class="d-flex ellipsis-name" style="height: 20px;">
                <div>
                  <span>증상: </span>
                </div>
                <div class="ellipsis-name" v-html="item.symptomDetail"></div>
              </div>
              <div class="ellipsis-name">
                <span>청구서: </span><span>{{ item.content }}</span>
              </div>
              <div class="ellipsis-name d-flex">
                <span>결제금액: </span><span class="flex-grow-1">{{ paymentMsg(item) }}</span>
                <button class="btn bg-primary" style="color: white" @click="iamport(item)">결제하기</button>
              </div>
            </div>
          </div>
        </div>
      </div>
      <!--      start 버튼 박스-->
      <div class="btn-util-box d-flex justify-content-center">
        <button class="util-btn left-util-btn btn bg-warning" @click="prevBtn" style="width: 120px; ">이전</button>
        <button class="util-btn btn bg-warning" @click="nextBtn" style="width: 120px;">홈으로</button>
      </div>
      <!--      end 버튼 박스-->
    </div>
  </section><!-- End Hero -->

</template>

<script>
import {mapMutations, mapState} from "vuex";
import axios from "axios";

const Swal = window.Swal;
const IMP = window.IMP;
//가맹점 식별코드
IMP.init('imp27148222');

export default {
  name: "ReceiptList",

  data() {
    return {
      receiptList: [], // 수납내역 리스트
    }
  },
  mounted() {
    this.getReceiptList();
    this.divHeightFix();
  },
  computed: {
    ...mapState('mobilePayment',
        ['patientData',]
    ),
  },
  methods: {
    ...mapMutations('mobilePayment', {
      setNextStep: 'setNextStep',
      initState: 'initState',
      setReceiptData: 'setReceiptData',


    }),
    paymentMsg(item) {
      let resultMsg = item.payment;
      // 숫자 3번째마다 , 찍기
      return resultMsg.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',') + "원";
    },
    divHeightFix() {
      let div = document.getElementById('mobileDoctor-box');
      let divHeight = div.offsetHeight;
      let windowHeight = window.innerHeight;

      if (divHeight < windowHeight) {
        div.style.height = '100vh';
      } else {
        div.style.height = 'auto';
      }
    },
    prevBtn() {
      this.initState();
      this.setNextStep(1);
    },
    nextBtn() {
      this.$router.push('/mobile/home');
    },
    dateMsg(item) {
      let dateTemp = new Date(item)
      let year = dateTemp.getFullYear();
      let month = dateTemp.getMonth() + 1;
      if (month < 10) {
        month = "0" + month;
      }
      let date = dateTemp.getDate();

      return year + "." + month + "." + date;
    },
    ageMsg(item) {
      let dateTemp = new Date(item)
      let dateNow = new Date();

      let tempYear = dateTemp.getFullYear();
      let nowYear = dateNow.getFullYear();
      let age = parseInt(nowYear) - parseInt(tempYear) + 1;
      return age;
    },

    // 대기환자 리스트 저장
    setReceiptList(items) {
      this.receiptList = [];
      items.forEach((item) => {
        this.receiptList.push(item);
      })
    },
    // 수납내역 리스트 세팅
    getReceiptList() {
      axios.post('/mobile/payment/getReceiptList_proc', {
        patientId: this.patientData.id,
      }).then(response => {
        let list = response.data;
        this.setReceiptList(list);
      }).catch(function (error) {
        console.log(error);
      });
    },

    // 휴대폰번호
    fixPhoneNum(phoneNum) {
      let newStr = phoneNum.replace(/^(\d{0,3})(\d{0,4})(\d{0,4})$/g, "$1-$2-$3").replace(/(-{1,2})$/g, "");
      return newStr;
    },

    test() {
      axios.post('/mobile/payment/payment_proc', {
        // impUid: rsp.imp_uid ,
        impUid: "imp_685957144268",
        patientId: this.patientData.id,
      }).then((response) => {
        console.log(response.data.response);
      }).catch(function (error) {
        console.log(error);
      })
    },
    iamport(item) {
      let getReceiptList = () => {
        this.getReceiptList()
      };
      let patientData = this.patientData;
      let receiptData = item;
      IMP.request_pay({
        pg: 'kakaopay',
        pay_method: 'kakaopay',
        merchant_uid: 'merchant_' + new Date().getTime(),
        name: '비트케어', //결제창에서 보여질 이름
        amount: item.payment, //실제 결제되는 가격
        buyer_name: this.patientData.name, // 결제자 이름
        buyer_tel: this.fixPhoneNum(this.patientData.phoneNumber), // 결제자 번호
      }, function (rsp) {
        // 결제검증
        axios.post('/mobile/payment/payment_proc', {
          impUid: rsp.imp_uid,
          patientId: patientData.id,
          historyId: receiptData.historyId,
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
              getReceiptList();
              // this.$router.push('/mobile/home');
            })
          } else {
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

        // var msg ="";
        // if ( rsp.success ) {
        //   msg = '결제가 완료되었습니다.';
        //   msg += '고유ID : ' + rsp.imp_uid;
        //   msg += '상점 거래ID : ' + rsp.merchant_uid;
        //   msg += '결제 금액 : ' + rsp.paid_amount;
        //   msg += '카드 승인번호 : ' + rsp.apply_num;
        // } else {
        //   msg = '결제에 실패하였습니다.';
        //   msg += '에러내용 : ' + rsp.error_msg;
        // }
        // alert(msg);
      });
    }

  },
}
</script>

<style scoped>
#mobile-payment {
  width: 100%;
  /*height: 100vh;*/
  background-color: #A1C7E0;
  background-size: cover;
  /* 배경이미지 반복여부 */
  background-repeat: no-repeat;
  /* 배경의 위치는 그 세션의 정가운데에 위치 */
  background-position: center;
  /* 더이상 요소가 스크롤될때 같이 스크롤되지않고 이미지가 뷰포트부분에 고정, 화면이 스크롤 되더라도 같이 움직이지않는구조로 만듬 */
  background-attachment: fixed;
  position: relative;
  padding: 0;
}

#mobile-payment:before {
  content: "";
  background: rgba(0, 0, 0, 0.6);
  position: absolute;
  bottom: 0;
  top: 0;
  left: 0;
  right: 0;
}

#mobileDoctor-box {
  padding-top: 60px;
  padding-bottom: 30px;
  margin: 0 3px;
}

#mobileDoctor-box .util {
  background: rgba(12, 11, 9, 0.7);
  color: white;
}

#mobileDoctor-box * {
  /*color: #cccccc;*/
  white-space: nowrap;
}

.receipt-list-box {
  overflow-y: scroll;
  height: calc(100vh - 200px);
}

.receipt-list-box::-webkit-scrollbar {
  width: 0;
}

.receipt-box {
  text-align: left;
  background-color: white;
  /*background: rgba(12, 11, 9, 0.7);*/
}

.title span:after {
  display: inline-block;
  content: '';
  width: 1px;
  height: 12px;
  background: #b9b9b9;
  margin: 0 5px 0px 5px;
}

.util {
  margin: 0 0 5px 0;
  padding: 5px;
  border: 1px solid #DBDFE5;
  /*border: 1px solid transparent;*/
  border-radius: 5px;
}

.border-box {
  margin: 0 0 5px 0;
  padding: 5px;
  border: 1px solid #DBDFE5;
  /*border: 1px solid transparent;*/
  border-radius: 5px;
}

.ellipsis-name {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 100%; /* 요소의 최대 너비를 지정합니다. */
}



p {
  margin: 0 !important;
}

/* 넘버패드 css ------------ */
/* Common button styles */
@media (min-width: 992px) {
  .util-btn {
    width: 250px !important;
    margin: 30px 0 0 0;
  }

  .left-util-btn {
    margin: 30px 265px 0 0;
  }
}

@media (max-width: 992px) {
  .util-btn {
    width: 180px !important;
    margin: 20px 0 0 0;
  }

  .left-util-btn {
    margin: 20px 95px 0 0;
  }
}

@media (max-width: 576px) {
  .util-btn {
    width: 135px !important;
    margin: 20px 0 0 0;
  }

  .left-util-btn {
    margin: 20px 95px 0 0;
  }
}

@media (max-width: 400px) {
  .util-btn {
    width: 100px !important;
    margin: 20px 0 0 0;
  }

  .left-util-btn {
    margin: 20px 40px 0 0;
  }
}

@media (max-width: 300px) {
  .util-btn {
    width: 100px !important;
    margin: 20px 0 0 0;
  }

  .left-util-btn {
    margin: 20px 20px 0 0;
  }

}

</style>
<template>
  <div>
    <div class="title-border-box d-flex">
      <span style="font-size: 1.2em; font-weight: 700; flex-grow: 1">수납 내역</span>
      <div class="refresh-img-box" style="cursor:pointer" @click="refreshBtn">
        <img src="@/assets/img/refresh.png">
      </div>
    </div>
    <div class="empty-box border-box" v-if="isSelectEmpty">
      <div class="empty-img-box">
        <img src="@/assets/img/empty-box.png">
      </div>
    </div>
    <div class="border-box" v-if="isReceiptEmpty">
      <div v-if="isRequestReceipt">
        <b-dropdown
            :text="visitMsg"
            block
            variant="primary"
            class="btn-outline-dark mt-2 mb-2"
            menu-class="w-100"
        >
          <b-dropdown-item v-for="(item) in visitList" :key="item.id" @click="dropClick(item)">{{ item.content }}
          </b-dropdown-item>
        </b-dropdown>
      </div>
      <div class="d-flex" v-if="isRequestReceipt">
        <label class="font-weight-bold flex-grow-1">총 진료비</label><span>{{ paymentMsg }}</span>
      </div>
      <div v-if="!isRequestReceipt">
        <div class="d-flex">
          <label class="font-weight-bold flex-grow-1">청구서</label><span>{{ receiptData.content }}</span>
        </div>
        <div class="d-flex">
          <label class="font-weight-bold flex-grow-1">총 진료비</label><span>{{ receiptPaymentMsg }}</span>
        </div>
      </div>
      <div>
        <button class="btn btn-primary w-100" v-if="isRequestReceipt" @click="receiptBtn" style="width: 120px">수납요청
        </button>
        <button class="btn btn-primary w-100" v-if="!isRequestReceipt" @click="cancleReceiptBtn" style="width: 120px">
          요청취소
        </button>
      </div>
    </div>
    <div class="main-border-box" v-if="isReceiptCard">
      <div><span class="font-weight-bold">카드</span></div>
      <div class="d-flex">
        <label class="font-weight-bold flex-grow-1">결제시각</label><span>2023.06.07</span>
      </div>
      <div class="d-flex">
        <label class="font-weight-bold flex-grow-1">수납금액</label><span>16,970</span>
      </div>
      <div class="d-flex">
        <label class="font-weight-bold flex-grow-1">카드번호</label><span>1111111</span>
      </div>
      <div class="d-flex">
        <label class="font-weight-bold flex-grow-1">승인번호</label><span>1111111</span>
      </div>
      <div class="d-flex">
        <label class="font-weight-bold flex-grow-1">카드종류</label><span>국민</span>
      </div>
    </div>
    <div class="main-border-box" v-if="isReceiptKakaopay">
      <div><span class="font-weight-bold">카카오페이</span></div>
      <div class="d-flex">
        <label class="font-weight-bold flex-grow-1">결제시각</label><span>2023.06.07</span>
      </div>
      <div class="d-flex">
        <label class="font-weight-bold flex-grow-1">수납금액</label><span>16,970</span>
      </div>
    </div>
  </div>
</template>

<script>
import {mapMutations, mapState} from "vuex";
import axios from "axios";

const Swal = window.Swal;

export default {
  name: "NurseReceipt",
  data() {
    return {
      receiptVisitList: [],
      content: "",
      payment: "",
    }
  },
  mounted() {
    // historyPage의 historyData 초기화
  },

  computed: {
    ...mapState('nurse',
        ['historyData', 'visitList', 'receiptData']
    ),
    visitMsg() {
      if (this.content === "") return "진료비를 선택해주세요.";
      else return this.content;
    },
    paymentMsg() {
      let resultMsg = this.payment;
      // 숫자 3번째마다 , 찍기
      return resultMsg.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',') + "원";
    },
    receiptPaymentMsg() {
      let resultMsg = this.receiptData.payment;
      // 숫자 3번째마다 , 찍기
      return resultMsg.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',') + "원";
    },
    // 진료기록 선택이 안되었을때 empty박스 보이기
    isSelectEmpty() {
      if (this.historyData === "") return true;
      else return false;
    },
    // 수납요청 페이지 보이기
    isReceiptEmpty() {
      if (this.historyData !== "" && (this.receiptData === "" || this.receiptData.impUid === null)) return true;
      else return false;
    },
    // 카드결제 내역 보이기
    isReceiptCard() {
      if (this.historyData !== "" && this.receiptData !== "" && this.receiptData.impUid !== null && this.receiptData.cardName !== null) return true;
      else return false;
    },
    // 카카오페이 내역 보이기
    isReceiptKakaopay() {
      if (this.historyData !== "" && this.receiptData !== "" && this.receiptData.impUid !== null && this.receiptData.cardName === null) return true;
      else return false;
    },
    isRequestReceipt() {
      return this.receiptData === "" ? true : false;
    }
  },
  methods: {
    ...mapMutations('nurse', {
      setReceiptData: 'setReceiptData',
      setVisitList: 'setVisitList',
      initHistoryData: 'initHistoryData',
    }),
    //   새로고침 버튼
    refreshBtn() {
      this.initHistoryData(); // 수납내역 데이터 초기화
      this.content = "";  // 청구내용 초기화
      this.payment = "";  // 청구내용 초기화
    },
    dropClick(item) {
      this.content = item.content;
      this.payment = item.payment;
    },
    //결제요청버튼
    receiptBtn() {
      if (this.content === "") {
        Swal.fire({
          icon: 'error',
          title: 'error',
          text: '진료비를 선택해주세요.',
          showConfirmButton: false,
          timer: 1000
        })
      } else {
        axios.post('/nurse/receipt_proc', {
          patientId: this.historyData.patientId,
          historyId: this.historyData.id,
          content: this.content,
          payment: this.payment,
        }).then((response) => {
          if (response.data === true) {
            Swal.fire({
              icon: 'success',
              title: 'success',
              text: '결제요청 완료',
              showConfirmButton: false,
              timer: 1000
            }).then(() => {
              this.getReceiptData(this.historyData.id);
            })
          } else {
            Swal.fire({
              icon: 'error',
              title: 'error',
              text: '이미 결제요청된 환자입니다.',
              showConfirmButton: false,
              timer: 1000
            }).then(() => {
              this.getReceiptData(this.historyData.id);
            })
          }
        }).catch(function (error) {
          console.log(error);
        })
      }
    },
    getReceiptData(historyId) {
      this.content = "";  // 청구내용 초기화
      this.payment = "";  // 청구내용 초기화
      return axios.post('/nurse/getReceiptData', {
        historyId: historyId,
      }).then(response => {
        let data = response.data;
        this.setReceiptData(data);
      }).catch(function (error) {
        console.log(error);
      });
    },
    // 결제요청취소버튼
    cancleReceiptBtn() {
      axios.post('/nurse/cancle_receipt_proc', {
        receiptId: this.receiptData.id,
      }).then((response) => {
        if (response.data === "결제요청완료") {
          Swal.fire({
            icon: 'success',
            title: 'success',
            text: '결제요청취소 완료',
            showConfirmButton: false,
            timer: 1000
          }).then(() => {
            this.getReceiptData(this.receiptData.historyId);
          })
        } else if (response.data === "요청완료된내역") {
          Swal.fire({
            icon: 'error',
            title: 'error',
            text: '결제요청취소 실패',
            showConfirmButton: false,
            timer: 1000
          }).then(() => {
            this.getReceiptData(this.receiptData.historyId);
          })
        } else if (response.data === "결제완료된내역") {
          Swal.fire({
            icon: 'error',
            title: 'error',
            text: '이미 결제완료된 내역입니다.',
            showConfirmButton: false,
            timer: 1000
          }).then(() => {
            this.getReceiptData(this.receiptData.historyId);
          })
        }
      }).catch(function (error) {
        console.log(error);
      })
    },
  },

}
</script>

<style scoped>
.refresh-img-box {
  width: 25px;
  margin: 0 auto;
  display: flex;
  vertical-align: middle;
  justify-content: center;
}

.refresh-img-box img {
  width: inherit;
  object-fit: contain;
}
.title-border-box {
  margin: 5px;
  padding: 0 5px;
  border-width: 2px;
  border-style: solid;
  border-color: #003A63;
  border-image: initial;
  border-radius: 10px;
}
.main-border-box {
  margin: 5px;
  padding: 0 5px;
  border-width: 1px;
  border-style: solid;
  border-color: #ccc;
  border-image: initial;
  border-radius: 10px;
}
.empty-box {
  height: 600px;
  display: flex;
}

.empty-img-box {
  width: 70px;
  margin: 0 auto;
  display: flex;
  vertical-align: middle;
  justify-content: center;
}

.empty-img-box img {
  width: inherit;
  object-fit: contain;
}
</style>

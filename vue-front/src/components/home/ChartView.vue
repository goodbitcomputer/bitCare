<template>
  <Bar
      :chart-options="chartOptions"
      :chart-data="chartData"
      :chart-id="chartId"
      :dataset-id-key="datasetIdKey"
      :plugins="plugins"
      :css-classes="cssClasses"
      :styles="styles"
      :width="width"
      :height="height"
  />
</template>

<script>
import { Bar } from 'vue-chartjs'

import {
  Chart as ChartJS,
  Title,
  Tooltip,
  Legend,
  BarElement,
  CategoryScale,
  LinearScale
} from 'chart.js'
import axios from "axios";
import {mapMutations, mapState} from "vuex";

ChartJS.register(Title, Tooltip, Legend, BarElement, CategoryScale, LinearScale)

export default {
  name: 'ChartView',
  components: {
    Bar
  },
  created(){
    this.getHistoryCount()
  },
  props: {
    chartId: {
      type: String,
      default: 'bar-chart'
    },
    datasetIdKey: {
      type: String,
      default: 'label'
    },
    width: {
      type: Number,
      default: 400
    },
    height: {
      type: Number,
      default: 400
    },
    cssClasses: {
      default: '',
      type: String
    },
    styles: {
      type: Object,
      default: () => {}
    },
    plugins: {
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      chartData: {
        labels: [
          '머리',
          '몸',
          '왼팔',
          '오른팔',
          '왼쪽 다리',
          '오른쪽 다리'
        ],
        datasets: [
          {
            label: '진료 기록 수',
            backgroundColor: '#777777',
            data: [
                this.$store.state.doctor.historyCountList[0],
                this.$store.state.doctor.historyCountList[1],
                this.$store.state.doctor.historyCountList[2],
                this.$store.state.doctor.historyCountList[3],
                this.$store.state.doctor.historyCountList[4],
                this.$store.state.doctor.historyCountList[5],
            ]
          }
        ]
      },
      chartOptions: {
        responsive: true,
        maintainAspectRatio: false
      }
    }
  },
  computed: {
    ...mapState('doctor',
      ['historyCountList']
    ),
  },
  methods: {
    ...mapMutations('doctor', {
      setHistoryCountList: 'setHistoryCountList'
    }),
    getHistoryCount(){
      axios.get('/doctor/getHistoryCount', {}).then(response => {
        let list = response.data
        this.setHistoryCountList(list)
      }).catch(function (error) {
        console.log(error);
      });
    },
  },
}
</script>

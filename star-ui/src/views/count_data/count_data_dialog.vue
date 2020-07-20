<template>
  <el-dialog class="xpy_dialog" :title="title" :visible="addRowVisible" :width="dialogWidth" :close-on-press-escape="dialogEsc" :close-on-click-modal="dialogClick" :before-close="beforClose">
    <el-form ref="form" :model="form" label-width="400px" size="mini">
      <el-collapse v-model="activeNames">
        <el-collapse-item title="机构配置信息" name="1">
          <el-form-item label="全日制ICU专科医师数">
            <el-input v-model.number="form.icuZkys" :readonly="readonly" />
          </el-form-item>
          <el-form-item label="ICU床位数">
            <el-input v-model.number="form.icuCws" :readonly="readonly" />
          </el-form-item>
          <el-form-item label="ICU护士总数">
            <el-input v-model.number="form.icuHszs" :readonly="readonly" />
          </el-form-item>
          <el-form-item label="医院总床位数">
            <el-input v-model.number="form.yyzcws" :readonly="readonly" />
          </el-form-item>
          <el-form-item>
            <span><span class="span_label">ICU床位率</span><span class="span_data">{{ icuCwl }}</span><span class="bank15" /></span>
            <span><span class="span_label">ICU医师床位比</span><span class="span_data">{{ icuYscwb }}</span><span class="bank15" /></span>
            <span><span class="span_label">ICU护士床位比</span><span class="span_data">{{ icuHscwb }}</span><span class="bank15" /></span>
          </el-form-item>
        </el-collapse-item>
        <el-collapse-item title="患者收治情况" name="2">
          <el-form-item label="ICU原有病人数">
            <el-input v-model.number="form.icuYybrs" :readonly="readonly" />
          </el-form-item>
          <el-form-item label="ICU新收治病人数">
            <el-input v-model.number="form.icuXszbrs" :readonly="readonly" />
          </el-form-item>
          <el-form-item>
            <span><span class="span_label">ICU收治患者总人数</span><span class="span_data">{{ icuSzhzzrs }}</span><span class="bank15" /></span>
          </el-form-item>
          <el-form-item label="医院原有病人数">
            <el-input v-model.number="form.yyYsbrs" :readonly="readonly" />
          </el-form-item>
          <el-form-item label="医院新收治病人数">
            <el-input v-model.number="form.yyXszbrs" :readonly="readonly" />
          </el-form-item>
          <el-form-item label="医院收治患者总人数">
            <span><span class="span_label">医院收治患者总人数</span><span class="span_data">{{ yySzhzzrs }}</span><span class="bank15" /></span>
          </el-form-item>
          <el-form-item>
            <span><span class="span_label">ICU患者收治率</span><span class="span_data">{{ icuHzszl }}</span><span class="bank15" /></span>
          </el-form-item>
          <el-form-item label="入ICU24小时内，APACHE Ⅱ评分 ≥ 15分患者">
            <el-input v-model.number="form.apacheHzs" :readonly="readonly" />
          </el-form-item>
          <el-form-item>
            <span><span class="span_label">急性生理与慢性健康评分（APACHE Ⅱ评分）≥ 15分患者收治率（入ICU24小时内）</span><span class="span_data">{{ apache }}</span><span class="bank15" /></span>
          </el-form-item>
          <el-form-item label="ICU患者收治总床日数">
            <el-input v-model.number="form.icuHzszzcrs" :readonly="readonly" />
          </el-form-item>
          <el-form-item label="医院收治患者总床日数">
            <el-input v-model.number="form.yySzzcrs" :readonly="readonly" />
          </el-form-item>
          <el-form-item label="">
            <span><span class="span_label">ICU患者收治床日率</span><span class="span_data">{{ icuHzszcrl }}</span><span class="bank15" /></span>
          </el-form-item>
        </el-collapse-item>
        <el-collapse-item title="Sepsis" name="3">
          <el-form-item label="入ICU诊断为Sepsis患者总数">
            <el-input v-model.number="form.sepsisHzzs" :readonly="readonly" />
          </el-form-item>
          <el-form-item label="全部完成3h bundle的患者数">
            <el-input v-model.number="form.tbundleHzs" :readonly="readonly" />
          </el-form-item>
          <el-form-item label="全部完成6h bundle的患者数">
            <el-input v-model.number="form.sbundleHzs" :readonly="readonly" />
          </el-form-item>
          <el-form-item>
            <span><span class="span_label">感染性休克3h集束化治疗（bundle）完成率</span><span class="span_data">{{ tbundleWcl }}</span><span class="bank15" /></span>
          </el-form-item>
          <el-form-item>
            <span><span class="span_label">感染性休克6h集束化治疗（bundle）完成率</span><span class="span_data">{{ sbundleWcl }}</span><span class="bank15" /></span>
          </el-form-item>
        </el-collapse-item>
        <el-collapse-item title="抗菌药物" name="4">
          <el-form-item label="治疗性抗菌药物病例总数">
            <el-input v-model.number="form.kjysblzs" :readonly="readonly" />
          </el-form-item>
          <el-form-item label="使用抗菌药物前病原学检验标本送检例数">
            <el-input v-model.number="form.kjywsjls" :readonly="readonly" />
          </el-form-item>
          <el-form-item>
            <span><span class="span_label">抗菌药物治疗前病原学送检率</span><span class="span_data">{{ kjywsjl }}</span><span class="bank15" /></span>
          </el-form-item>
        </el-collapse-item>
        <el-collapse-item title="DVT" name="5">
          <el-form-item label="采取药物预防、非药物预防DVT措施的病例总数">
            <el-input v-model.number="form.dvtblzs" :readonly="readonly" />
          </el-form-item>
          <el-form-item>
            <span><span class="span_label">深静脉血栓（DVT）预防率</span><span class="span_data">{{ dvtyfl }}</span><span class="bank15" /></span>
          </el-form-item>
        </el-collapse-item>
        <el-collapse-item title="患者病死率" name="6">
          <el-form-item label="临床死亡人数">
            <el-input v-model.number="form.lcswrs" :readonly="readonly" />
          </el-form-item>
          <el-form-item label="自动出院人数">
            <el-input v-model.number="form.zdcyrs" :readonly="readonly" />
          </el-form-item>
          <el-form-item>
            <span><span class="span_label">ICU总死亡人数</span><span class="span_data">{{ icuzswrs }}</span><span class="bank15" /></span>
          </el-form-item>
          <el-form-item label="ICU收治患者入ICU预计病死率之和">
            <el-input v-model="form.icuYjbslzh" :readonly="readonly" />
          </el-form-item>
          <el-form-item>
            <span><span class="span_label">ICU患者实际病死率</span><span class="span_data">{{ icuSjbsl }}</span><span class="bank15" /></span>
            <span><span class="span_label">ICU患者预计病死率</span><span class="span_data">{{ icuYjbsl }}</span><span class="bank15" /></span>
            <span><span class="span_label">ICU重症患者标化病死指数（Standardized Mortality Ratio）</span><span class="span_data">{{ icuBszs }}</span><span class="bank15" /></span>
          </el-form-item>
        </el-collapse-item>
        <el-collapse-item title="非计划转入和重返事件" name="7">
          <el-form-item label="非计划转入ICU患者数（术后）">
            <el-input v-model.number="form.fjhIcuHzs" :readonly="readonly" />
          </el-form-item>
          <el-form-item label="转入ICU患者总数（术后）">
            <el-input v-model.number="form.zrIcuHzs" :readonly="readonly" />
          </el-form-item>
          <el-form-item label="转出ICU后48h内重返ICU患者数">
            <el-input v-model.number="form.cfIcuHzs" :readonly="readonly" />
          </el-form-item>
          <el-form-item label="ICU转出患者总数">
            <el-input v-model.number="form.zcIcuHzs" :readonly="readonly" />
          </el-form-item>
          <el-form-item>
            <span><span class="span_label">非计划转入ICU率</span><span class="span_data">{{ fjhIcuZrl }}</span><span class="bank15" /></span>
            <span><span class="span_label">转出ICU后48h内重返率</span><span class="span_data">{{ cfl }}</span><span class="bank15" /></span>
          </el-form-item>
        </el-collapse-item>
        <el-collapse-item title="气管插管事件" name="8">
          <el-form-item label="非计划气管插管脱出次数">
            <el-input v-model.number="form.fjhTccs" :readonly="readonly" />
          </el-form-item>
          <el-form-item label="ICU患者气管插管总例数">
            <el-input v-model.number="form.icuCgzls" :readonly="readonly" />
          </el-form-item>
          <el-form-item>
            <span><span class="span_label">非计划气管插管拔管率</span><span class="span_data">{{ fjhBgl }}</span><span class="bank15" /></span>
          </el-form-item>
          <el-form-item label="计划拔管后48h内再插管例数">
            <el-input v-model.number="form.zcgls" :readonly="readonly" />
          </el-form-item>
          <el-form-item label="ICU患者气管插管拔管总例数(不包括非计划拔管患者)">
            <el-input v-model.number="form.icuBgzls" :readonly="readonly" />
          </el-form-item>
          <el-form-item>
            <span><span class="span_label">气管插管拔管后48h内再插管率</span><span class="span_data">{{ zcgl }}</span><span class="bank15" /></span>
          </el-form-item>
        </el-collapse-item>
        <el-collapse-item title="三管感染率" name="9">
          <el-form-item label="ICU呼吸机相关肺炎病人总例数">
            <el-input v-model.number="form.icuFybrzls" :readonly="readonly" />
          </el-form-item>
          <el-form-item label="ICU总机械通气天数">
            <el-input v-model.number="form.icuTqts" :readonly="readonly" />
          </el-form-item>
          <el-form-item>
            <span><span class="span_label">ICU呼吸机相关性肺炎（VAP）发病率</span><span class="span_data">{{ vapFbl }}</span><span class="bank15" /></span>
          </el-form-item>
          <el-form-item label="ICU中心静脉导管相关性血流感染总例数">
            <el-input v-model.number="form.icuXlglzls" :readonly="readonly" />
          </el-form-item>
          <el-form-item label="ICU中心静脉导管总天数">
            <el-input v-model.number="form.icuJmdgzts" :readonly="readonly" />
          </el-form-item>
          <el-form-item>
            <span><span class="span_label">ICU血管内导管相关血流感染（CRBSI）发病率</span><span class="span_data">{{ crbsiFbl }}</span><span class="bank15" /></span>
          </el-form-item>
          <el-form-item label="ICU导尿管相关泌尿系统感染总例数">
            <el-input v-model.number="form.icuMnglzls" :readonly="readonly" />
          </el-form-item>
          <el-form-item label="ICU尿管留置总天数">
            <el-input v-model.number="form.icuNglzzts" :readonly="readonly" />
          </el-form-item>
          <el-form-item>
            <span><span class="span_label">ICU导尿管相关泌尿系感染（CAUTI）发病率</span><span class="span_data">{{ cautiFbl }}</span><span class="bank15" /></span>
          </el-form-item>
        </el-collapse-item>
      </el-collapse>
    </el-form>
    <div v-if="!disabled" slot="footer">
      <el-button @click="cancle">关闭</el-button>
    </div>
  </el-dialog>
</template>
<script>
export default {
  name: 'OrgManagementAdd',
  props: {
    row: {
      type: Object,
      default: function() {
        return {}
      }
    },
    addRowVisible: {
      type: Boolean,
      default: function() {
        return false
      }
    },
    disabled: {
      type: Boolean,
      default: function() {
        return false
      }
    }
  },
  data() {
    return {
      // 表单form
      form: {},
      // 公共配置
      formSize: this.$config.formSize,
      buttonSize: this.$config.buttonSize,
      formFieldWidth: this.$config.formFieldWidth,
      formLabelWidth: this.$config.formLabelWidth,
      formLabelPosition: this.$config.formLabelPosition,
      dialogEsc: this.$config.dialogEsc,
      dialogClick: this.$config.dialogClick,
      dialogWidth: this.$config.dialogWidth,

      activeNames: ['1', '2', '3', '4', '5', '6', '7', '8', '9'],
      loading: false,
      readonly: true
    }
  },
  computed: {
    // 标题
    title: function() {
      // 查看
      return '查看' + this.form.sbsj
    },
    // ICU床位率     ICU床位数/医院总床位数
    icuCwl: function() {
      if (!this.$isEmpty(this.form.icuCws) && !this.$isEmpty(this.form.yyzcws) && this.form.yyzcws !== 0) {
        var cwl = (this.form.icuCws / this.form.yyzcws).toFixed(5)
        return (Math.round(cwl * 10000) / 100).toFixed(2) + '%'
      }
      return ''
    },
    // ICU医师床位比      全日制ICU专科医师数/ICU床位数
    icuYscwb: function() {
      if (!this.$isEmpty(this.form.icuZkys) && !this.$isEmpty(this.form.icuCws) && this.form.icuCws !== 0) {
      // var cwl = (this.form.icuZkys / this.form.icuCws).toFixed(5)
        return this.$decimalPercentConvert(this.form.icuZkys, this.form.icuCws)
      }
      return ''
    },
    // ICU护士床位比      ICU护士总数/ICU床位数
    icuHscwb: function() {
      if (!this.$isEmpty(this.form.icuHszs) && !this.$isEmpty(this.form.icuCws) && this.form.icuCws !== 0) {
      // var cwl = (this.form.icuZkys / this.form.icuCws).toFixed(5)
        return this.$decimalPercentConvert(this.form.icuHszs, this.form.icuCws)
      }
      return ''
    },
    // ICU收治患者总人数    ICU原有病人数 + ICU新收治病人数
    icuSzhzzrs: function() {
      var total = 0
      if (!this.$isEmpty(this.form.icuYybrs)) {
        total += this.form.icuYybrs
      }
      if (!this.$isEmpty(this.form.icuXszbrs)) {
        total += this.form.icuXszbrs
      }
      return total
    },
    // 医院收治患者总人数    医院原有病人数 + 医院新收治病人数
    yySzhzzrs: function() {
      var total = 0
      if (!this.$isEmpty(this.form.yyYsbrs)) {
        total += this.form.yyYsbrs
      }
      if (!this.$isEmpty(this.form.yyXszbrs)) {
        total += this.form.yyXszbrs
      }
      return total
    },
    // ICU患者收治率      ICU收治患者总人数/医院收治患者总人数
    icuHzszl: function() {
      if (!this.$isEmpty(this.icuSzhzzrs) && !this.$isEmpty(this.yySzhzzrs) && this.yySzhzzrs !== 0) {
        var bsf = (this.icuSzhzzrs / this.yySzhzzrs).toFixed(5)
        return (Math.round(bsf * 10000) / 100).toFixed(2) + '%'
      }
      return ''
    },
    // //急性生理与慢性健康评分（APACHE Ⅱ评分）≥ 15分患者收治率（入ICU24小时内）    入ICU24小时内，APACHE Ⅱ评分 ≥ 15分患者/ICU收治患者总人数
    apache: function() {
      if (!this.$isEmpty(this.form.apacheHzs) && !this.$isEmpty(this.icuSzhzzrs) && this.icuSzhzzrs !== 0) {
        var bsf = (this.form.apacheHzs / this.icuSzhzzrs).toFixed(5)
        return (Math.round(bsf * 10000) / 100).toFixed(2) + '%'
      }
      return ''
    },
    // ICU患者收治床日率           ICU患者收治总床日数/医院收治患者总床日数
    icuHzszcrl: function() {
      if (!this.$isEmpty(this.form.icuHzszzcrs) && !this.$isEmpty(this.form.yySzzcrs) && this.form.yySzzcrs !== 0) {
        var bsf = (this.form.icuHzszzcrs / this.form.yySzzcrs).toFixed(5)
        return (Math.round(bsf * 10000) / 100).toFixed(2) + '%'
      }
      return ''
    },
    // 感染性休克3h集束化治疗（bundle）完成率         全部完成3h bundle的患者数/入ICU诊断为Sepsis患者总数
    tbundleWcl: function() {
      if (!this.$isEmpty(this.form.tbundleHzs) && !this.$isEmpty(this.form.sepsisHzzs) && this.form.sepsisHzzs !== 0) {
        var bsf = (this.form.tbundleHzs / this.form.sepsisHzzs).toFixed(5)
        return (Math.round(bsf * 10000) / 100).toFixed(2) + '%'
      }
      return ''
    },
    // 感染性休克6h集束化治疗（bundle）完成率        全部完成6h bundle的患者数/入ICU诊断为Sepsis患者总数
    sbundleWcl: function() {
      if (!this.$isEmpty(this.form.sbundleHzs) && !this.$isEmpty(this.form.sepsisHzzs) && this.form.sepsisHzzs !== 0) {
        var bsf = (this.form.sbundleHzs / this.form.sepsisHzzs).toFixed(5)
        return (Math.round(bsf * 10000) / 100).toFixed(2) + '%'
      }
      return ''
    },
    // 抗菌药物治疗前病原学送检率             使用抗菌药物前病原学检验标本送检例数/治疗性抗菌药物病例总数
    kjywsjl: function() {
      if (!this.$isEmpty(this.form.kjywsjls) && !this.$isEmpty(this.form.kjysblzs) && this.form.kjysblzs !== 0) {
        var bsf = (this.form.kjywsjls / this.form.kjysblzs).toFixed(5)
        return (Math.round(bsf * 10000) / 100).toFixed(2) + '%'
      }
      return ''
    },
    // 深静脉血栓（DVT）预防率          采取药物预防、非药物预防DVT措施的病例总数/ICU收治患者总人数
    dvtyfl: function() {
      if (!this.$isEmpty(this.form.dvtblzs) && !this.$isEmpty(this.icuSzhzzrs) && this.icuSzhzzrs !== 0) {
        var bsf = (this.form.dvtblzs / this.icuSzhzzrs).toFixed(5)
        return (Math.round(bsf * 10000) / 100).toFixed(2) + '%'
      }
      return ''
    },
    // ICU总死亡人数                临床死亡人数 + 自动出院人数
    icuzswrs: function() {
      var total = 0
      if (!this.$isEmpty(this.form.lcswrs)) {
        total += this.form.lcswrs
      }
      if (!this.$isEmpty(this.form.zdcyrs)) {
        total += this.form.zdcyrs
      }
      return total
    },
    // ICU患者实际病死率           ICU总死亡人数/ICU收治患者总人数
    icuSjbsl: function() {
      if (!this.$isEmpty(this.icuzswrs) && !this.$isEmpty(this.icuSzhzzrs) && this.icuSzhzzrs !== 0) {
        var bsf = (this.icuzswrs / this.icuSzhzzrs).toFixed(5)
        return (Math.round(bsf * 10000) / 100).toFixed(2) + '%'
      }
      return ''
    },
    // ICU患者预计病死率           ICU收治患者入ICU预计病死率之和/ICU收治患者总人数
    icuYjbsl: function() {
      if (!this.$isEmpty(this.form.icuYjbslzh) && !this.$isEmpty(this.icuSzhzzrs) && this.icuSzhzzrs !== 0) {
        var bsf = (this.form.icuYjbslzh / this.icuSzhzzrs).toFixed(5)
        return (Math.round(bsf * 10000) / 100).toFixed(2) + '%'
      }
      return ''
    },
    // ICU重症患者标化病死指数（Standardized Mortality Ratio）        ICU总死亡人数/ICU收治患者总人数/ICU收治患者入ICU预计病死率之和/ICU收治患者总人数
    icuBszs: function() {
      if (!this.$isEmpty(this.icuzswrs) && !this.$isEmpty(this.icuSzhzzrs) && this.icuSzhzzrs !== 0 && !this.$isEmpty(this.form.icuYjbslzh) && this.form.icuYjbslzh !== 0 &&
      !this.$isEmpty(this.icuSzhzzrs) && this.icuSzhzzrs !== 0) {
        var bsf = ((this.icuzswrs / this.icuSzhzzrs) / (this.form.icuYjbslzh / this.icuSzhzzrs)).toFixed(5)
        return (Math.round(bsf * 10000) / 100).toFixed(2) + '%'
      }
      return ''
    },
    // 非计划转入ICU率          非计划转入ICU患者数（术后）/转入ICU患者总数（术后）
    fjhIcuZrl: function() {
      if (!this.$isEmpty(this.form.fjhIcuHzs) && !this.$isEmpty(this.form.zrIcuHzs) && this.form.zrIcuHzs !== 0) {
        var bsf = (this.form.fjhIcuHzs / this.form.zrIcuHzs).toFixed(5)
        return (Math.round(bsf * 10000) / 100).toFixed(2) + '%'
      }
      return ''
    },
    // 转出ICU后48h内重返率        转出ICU后48h内重返ICU患者数/ICU转出患者总数
    cfl: function() {
      if (!this.$isEmpty(this.form.cfIcuHzs) && !this.$isEmpty(this.form.zcIcuHzs) && this.form.zcIcuHzs !== 0) {
        var bsf = (this.form.cfIcuHzs / this.form.zcIcuHzs).toFixed(5)
        return (Math.round(bsf * 10000) / 100).toFixed(2) + '%'
      }
      return ''
    },
    // 非计划气管插管拔管率         非计划气管插管脱出次数/ICU患者气管插管总例数
    fjhBgl: function() {
      if (!this.$isEmpty(this.form.fjhTccs) && !this.$isEmpty(this.form.icuCgzls) && this.form.icuCgzls !== 0) {
        var bsf = (this.form.fjhTccs / this.form.icuCgzls).toFixed(5)
        return (Math.round(bsf * 10000) / 100).toFixed(2) + '%'
      }
      return ''
    },
    // 气管插管拔管后48h内再插管率       计划拔管后48h内再插管例数/ICU患者气管插管拔管总例数(不包括非计划拔管患者)
    zcgl: function() {
      if (!this.$isEmpty(this.form.zcgls) && !this.$isEmpty(this.form.icuBgzls) && this.form.icuBgzls !== 0) {
        var bsf = (this.form.zcgls / this.form.icuBgzls).toFixed(5)
        return (Math.round(bsf * 10000) / 100).toFixed(2) + '%'
      }
      return ''
    },
    // ICU呼吸机相关性肺炎（VAP）发病率         ICU呼吸机相关肺炎病人总例数/ICU总机械通气天数
    vapFbl: function() {
      if (!this.$isEmpty(this.form.icuFybrzls) && !this.$isEmpty(this.form.icuTqts) && this.form.icuTqts !== 0) {
        var bsf = (this.form.icuFybrzls / this.form.icuTqts).toFixed(5)
        return (Math.round(bsf * 10000) / 100).toFixed(2) + '%'
      }
      return ''
    },
    // ICU血管内导管相关血流感染（CRBSI）发病率        ICU中心静脉导管相关性血流感染总例数/ICU中心静脉导管总天数
    crbsiFbl: function() {
      if (!this.$isEmpty(this.form.icuXlglzls) && !this.$isEmpty(this.form.icuJmdgzts) && this.form.icuJmdgzts !== 0) {
        var bsf = (this.form.icuXlglzls / this.form.icuJmdgzts).toFixed(5)
        return (Math.round(bsf * 10000) / 100).toFixed(2) + '%'
      }
      return ''
    },
    // ICU导尿管相关泌尿系感染（CAUTI）发病率        ICU导尿管相关泌尿系统感染总例数/ICU尿管留置总天数
    cautiFbl: function() {
      if (!this.$isEmpty(this.form.icuMnglzls) && !this.$isEmpty(this.form.icuNglzzts) && this.form.icuNglzzts !== 0) {
        var bsf = (this.form.icuMnglzls / this.form.icuNglzzts).toFixed(5)
        return (Math.round(bsf * 10000) / 100).toFixed(2) + '%'
      }
      return ''
    }
  },
  watch: {
    $route: {
      handler: function(route) {
        this.redirect = route.query && route.query.redirect
      },
      immediate: true
    },
    row(v) {
      this.form = JSON.parse(JSON.stringify(v))
    }
  },
  mounted() {},
  created() {},
  destroyed() {},
  methods: {
    clearFields() {
      this.$refs['form'].resetFields()
    },
    cancle() {
      this.$emit('update', false)
    },
    /**
      * 关闭之前的调用，需要更新父组件，进行关闭模态框
      */
    beforClose() {
      this.$emit('update', false)
    }
  }
}
</script>
<style lang="less">
span.bank2
{
 padding-left:2px;
}
span.bank5
{
 padding-left:5px;
}
span.bank15
{
 padding-left:15px;
}
.span_label{
  font-weight: bold;color: blue;
}
.span_data{
  font-style: italic;color: red;font-weight: bold;
}
.xpy_dialog {
  display: flex;
  justify-content: center;
  align-items: Center;
  overflow: hidden;
  .el-dialog {
    margin: 0 auto !important;
    max-height: 90%;
    height: 90%;
    overflow: hidden;
    .el-dialog__body {
      position: absolute;
      left: 0;
      top: 61px;
      bottom: 61px;
      right: 10px;
      padding: 0;
      z-index: 1;
      overflow: hidden;
      overflow-y: auto;
    }
    .el-dialog__footer{
      position: absolute;
      left: 0;
      right: 0;
      bottom: 0;
    }
  }
}
</style>

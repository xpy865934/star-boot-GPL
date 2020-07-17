<template>
  <div class="app-container">
    <el-collapse v-model="activeNames">
      <el-collapse-item title="机构配置信息" name="1">
        <el-form ref="form_jgpz" :model="form" label-width="400px" size="mini">
          <el-form-item label="全日制ICU专科医师数">
            <el-input v-model="form.icuPhysicianNum" />
          </el-form-item>
          <el-form-item label="ICU床位数">
            <el-input v-model="form.icuBedNum" />
          </el-form-item>
          <el-form-item label="ICU护士总数">
            <el-input v-model="form.icuNurseNum" />
          </el-form-item>
          <el-form-item label="医院总床位数">
            <el-input v-model="form.totalBedNum" />
          </el-form-item>
          <el-form-item>
            <span><span class="span_label">ICU床位率</span><span class="span_data">{{ icuBedRate }}</span><span class="bank15" /></span>
            <span><span class="span_label">ICU医师床位比</span><span class="span_data">{{ form.icuPhysicianBedRate }}</span><span class="bank15" /></span>
            <span><span class="span_label">ICU护士床位比</span><span class="span_data">{{ form.icuNurseBedRate }}</span><span class="bank15" /></span>
          </el-form-item>
        </el-form>
      </el-collapse-item>
      <el-collapse-item title="患者收治情况" name="2">
        <el-form ref="form" :model="form" label-width="400px" size="mini">
          <el-form-item label="ICU原有病人数">
            <el-input v-model="form.desc" />
          </el-form-item>
          <el-form-item label="ICU新收治病人数">
            <el-input v-model="form.desc" />
          </el-form-item>
          <el-form-item label="ICU收治患者总人数">
            <el-input v-model="form.desc" />
          </el-form-item>
          <el-form-item label="医院原有病人数">
            <el-input v-model="form.desc" />
          </el-form-item>
          <el-form-item label="医院新收治病人数">
            <el-input v-model="form.desc" />
          </el-form-item>
          <el-form-item label="医院收治患者总人数">
            <el-input v-model="form.desc" />
          </el-form-item>
          <el-form-item>
            <span><span class="span_label">ICU患者收治率</span><span class="span_data">12%</span><span class="bank15" /></span>
          </el-form-item>
          <el-form-item label="入ICU24小时内，APACHE Ⅱ评分 ≥ 15分患者">
            <el-input v-model="form.desc" />
          </el-form-item>
          <el-form-item>
            <span><span class="span_label">急性生理与慢性健康评分（APACHE Ⅱ评分）≥ 15分患者收治率（入ICU24小时内）</span><span class="span_data">12%</span><span class="bank15" /></span>
          </el-form-item>
          <el-form-item label="ICU患者收治总床日数">
            <el-input v-model="form.desc" />
          </el-form-item>
          <el-form-item label="医院收治患者总床日数">
            <el-input v-model="form.desc" />
          </el-form-item>
          <el-form-item label="">
            <span><span class="span_label">ICU患者收治床日率</span><span class="span_data">12%</span><span class="bank15" /></span>
          </el-form-item>
        </el-form>
      </el-collapse-item>
      <el-collapse-item title="Sepsis" name="3">
        <el-form ref="form" :model="form" label-width="400px" size="mini">
          <el-form-item label="入ICU诊断为Sepsis患者总数">
            <el-input v-model="form.desc" />
          </el-form-item>
          <el-form-item label="全部完成3h bundle的患者数">
            <el-input v-model="form.desc" />
          </el-form-item>
          <el-form-item label="全部完成6h bundle的患者数">
            <el-input v-model="form.desc" />
          </el-form-item>
          <el-form-item>
            <span><span class="span_label">感染性休克3h集束化治疗（bundle）完成率</span><span class="span_data">12%</span><span class="bank15" /></span>
          </el-form-item>
          <el-form-item>
            <span><span class="span_label">感染性休克6h集束化治疗（bundle）完成率</span><span class="span_data">12%</span><span class="bank15" /></span>
          </el-form-item>
        </el-form>
      </el-collapse-item>
      <el-collapse-item title="抗菌药物" name="4">
        <el-form ref="form" :model="form" label-width="400px" size="mini">
          <el-form-item label="治疗性抗菌药物病例总数">
            <el-input v-model="form.desc" />
          </el-form-item>
          <el-form-item label="使用抗菌药物前病原学检验标本送检例数">
            <el-input v-model="form.desc" />
          </el-form-item>
          <el-form-item>
            <span><span class="span_label">抗菌药物治疗前病原学送检率</span><span class="span_data">12%</span><span class="bank15" /></span>
          </el-form-item>
        </el-form>
      </el-collapse-item>
      <el-collapse-item title="DVT" name="5">
        <el-form ref="form" :model="form" label-width="400px" size="mini">
          <el-form-item label="采取药物预防、非药物预防DVT措施的病例总数">
            <el-input v-model="form.desc" />
          </el-form-item>
          <el-form-item>
            <span><span class="span_label">深静脉血栓（DVT）预防率</span><span class="span_data">12%</span><span class="bank15" /></span>
          </el-form-item>
        </el-form>
      </el-collapse-item>
      <el-collapse-item title="患者病死率" name="6">
        <el-form ref="form" :model="form" label-width="400px" size="mini">
          <el-form-item label="临床死亡人数">
            <el-input v-model="form.desc" />
          </el-form-item>
          <el-form-item label="自动出院人数">
            <el-input v-model="form.desc" />
          </el-form-item>
          <el-form-item>
            <span><span class="span_label">ICU总死亡人数</span><span class="span_data">12%</span><span class="bank15" /></span>
          </el-form-item>
          <el-form-item label="ICU收治患者入ICU预计病死率之和">
            <el-input v-model="form_jgpz.desc" />
          </el-form-item>
          <el-form-item>
            <span><span class="span_label">ICU患者实际病死率</span><span class="span_data">12%</span><span class="bank15" /></span>
            <span><span class="span_label">ICU患者预计病死率</span><span class="span_data">12%</span><span class="bank15" /></span>
            <span><span class="span_label">ICU重症患者标化病死指数（Standardized Mortality Ratio）</span><span class="span_data">12%</span><span class="bank15" /></span>
          </el-form-item>
        </el-form>
      </el-collapse-item>
      <el-collapse-item title="非计划转入和重返事件" name="7">
        <el-form ref="form" :model="form" label-width="400px" size="mini">
          <el-form-item label="非计划转入ICU患者数（术后）">
            <el-input v-model="form.desc" />
          </el-form-item>
          <el-form-item label="转入ICU患者总数（术后）">
            <el-input v-model="form.desc" />
          </el-form-item>
          <el-form-item label="转出ICU后48h内重返ICU患者数">
            <el-input v-model="form.desc" />
          </el-form-item>
          <el-form-item label="ICU转出患者总数">
            <el-input v-model="form.desc" />
          </el-form-item>
          <el-form-item>
            <span><span class="span_label">非计划转入ICU率</span><span class="span_data">12%</span><span class="bank15" /></span>
            <span><span class="span_label">转出ICU后48h内重返率</span><span class="span_data">12%</span><span class="bank15" /></span>
          </el-form-item>
        </el-form>
      </el-collapse-item>
      <el-collapse-item title="气管插管事件" name="8">
        <el-form ref="form" :model="form" label-width="400px" size="mini">
          <el-form-item label="非计划气管插管脱出次数">
            <el-input v-model="form.desc" />
          </el-form-item>
          <el-form-item label="ICU患者气管插管总例数">
            <el-input v-model="form.desc" />
          </el-form-item>
          <el-form-item>
            <span><span class="span_label">非计划气管插管拔管率</span><span class="span_data">12%</span><span class="bank15" /></span>
          </el-form-item>
          <el-form-item label="计划拔管后48h内再插管例数">
            <el-input v-model="form.desc" />
          </el-form-item>
          <el-form-item label="ICU患者气管插管拔管总例数(不包括非计划拔管患者)">
            <el-input v-model="form.desc" />
          </el-form-item>
          <el-form-item>
            <span><span class="span_label">气管插管拔管后48h内再插管率</span><span class="span_data">12%</span><span class="bank15" /></span>
          </el-form-item>
        </el-form>
      </el-collapse-item>
      <el-collapse-item title="三管感染率" name="9">
        <el-form ref="form" :model="form" label-width="400px" size="mini">
          <el-form-item label="ICU呼吸机相关肺炎病人总例数">
            <el-input v-model="form.desc" />
          </el-form-item>
          <el-form-item label="ICU总机械通气天数">
            <el-input v-model="form.desc" />
          </el-form-item>
          <el-form-item>
            <span><span class="span_label">ICU呼吸机相关性肺炎（VAP）发病率</span><span class="span_data">12%</span><span class="bank15" /></span>
          </el-form-item>
          <el-form-item label="ICU中心静脉导管相关性血流感染总例数">
            <el-input v-model="form.desc" />
          </el-form-item>
          <el-form-item label="ICU中心静脉导管总天数">
            <el-input v-model="form.desc" />
          </el-form-item>
          <el-form-item>
            <span><span class="span_label">ICU血管内导管相关血流感染（CRBSI）发病率</span><span class="span_data">12%</span><span class="bank15" /></span>
          </el-form-item>
          <el-form-item label="ICU导尿管相关泌尿系统感染总例数">
            <el-input v-model="form.desc" />
          </el-form-item>
          <el-form-item label="ICU尿管留置总天数">
            <el-input v-model="form.desc" />
          </el-form-item>
          <el-form-item>
            <span><span class="span_label">ICU导尿管相关泌尿系感染（CAUTI）发病率</span><span class="span_data">12%</span><span class="bank15" /></span>
          </el-form-item>
        </el-form>
      </el-collapse-item>
    </el-collapse>
    <el-form ref="form" :model="form" style="text-align: center;">
      <el-form-item>
        <el-button type="primary" @click="submitForm('ruleForm')">暂存</el-button><span class="bank15" /><span class="bank15" />
        <el-button type="danger" @click="submitForm('ruleForm')">上报</el-button><span class="bank15" /><span class="bank15" />
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
export default {
  data() {
    return {
      activeNames: ['1', '2', '3', '4', '5', '6', '7', '8', '9'],
      form_jgpz: {},
      form: {
        name: '',
        region: '',
        date1: '',
        date2: '',
        delivery: false,
        type: [],
        resource: '',
        desc: ''
      }
    }
  },
  computed: {
    icuBedRate: function() {
      return ''
    }
  },
  methods: {
    onSubmit() {
      this.$message('submit!')
    },
    onCancel() {
      this.$message({
        message: 'cancel!',
        type: 'warning'
      })
    }
  }
}
</script>

<style scoped>
.line{
  text-align: center;
}
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
</style>


<template>
  <div class="app-container">
    <el-form ref="form" :model="form" label-width="400px" size="mini">
      <el-collapse v-model="activeNames">

        <el-collapse-item title="医院基本信息" name="1">
          <el-form-item label="医院名称(中文)">
            <el-input v-model="form.userName" disabled />
          </el-form-item>
          <el-form-item label="医院名称(英文)">
            <el-input v-model="form.userCode" disabled />
          </el-form-item>
          <el-form-item
            label="省市区"
            prop="proviceSelect"
          >
            <el-cascader
              v-model="form.proviceSelect"
              :options="areaData"
              :change-on-select="true"
              :clearable="true"
              :filterable="true"
              placeholder="请选择"
              :style="{ width: this.$config.formFieldWidth }"
              @change="handleChange"
            />
          </el-form-item>
          <el-form-item label="详细地址">
            <el-input v-model="form.address" />
          </el-form-item>
          <el-form-item label="邮编">
            <el-input v-model="form.postalCode" />
          </el-form-item>
        </el-collapse-item>
        <el-collapse-item title="医院ICU基本信息" name="2">
          <el-row style="text-align:center">
            <el-col>医院等级及床位数</el-col>
          </el-row>
          <el-form-item label="医院等级">
            <el-input v-model="form.level" />
          </el-form-item>
          <el-form-item label="医院总床位数">
            <el-input v-model="form.bedTotal" />
          </el-form-item>
          <el-form-item label="ICU床位数">
            <el-input v-model="form.icuBedTotal" />
          </el-form-item>
          <el-row style="text-align:center">
            <el-col>全日制ICU专科医师数</el-col>
          </el-row>
          <el-form-item label="ICU主任医师人数">
            <el-input v-model="form.icuZrys" />
          </el-form-item>
          <el-form-item label="ICU副主任医师人数">
            <el-input v-model="form.icuFzrys" />
          </el-form-item>
          <el-form-item label="ICU主治医师人数">
            <el-input v-model="form.icuZzys" />
          </el-form-item>
          <el-form-item label="ICU住院医师人数">
            <el-input v-model="form.icuZyys" />
          </el-form-item>
          <el-form-item label="ICU呼吸治疗师人数">
            <el-input v-model="form.icuHxzls" />
          </el-form-item>
          <el-form-item label="ICU临床药师人数">
            <el-input v-model="form.icuLcys" />
          </el-form-item>
          <el-form-item label="ICU物理治疗师人数">
            <el-input v-model="form.icuWlzl" />
          </el-form-item>
          <el-row style="text-align:center">
            <el-col>ICU护士数</el-col>
          </el-row>
          <el-form-item label="ICU主任护师人数">
            <el-input v-model="form.icuZrhs" />
          </el-form-item>
          <el-form-item label="ICU副主任护师人数">
            <el-input v-model="form.icuFZrhs" />
          </el-form-item>
          <el-form-item label="ICU主管护师人数">
            <el-input v-model="form.icuZghs" />
          </el-form-item>
          <el-form-item label="ICU护师人数">
            <el-input v-model="form.icuHsy" />
          </el-form-item>
          <el-form-item label="ICU护士人数">
            <el-input v-model="form.icuHs" />
          </el-form-item>
        </el-collapse-item>
        <el-collapse-item title="质控联络信息" name="3">
          <el-form-item label="联络人">
            <el-input v-model="form.lxrName" />
          </el-form-item>
          <el-form-item label="联络手机">
            <el-input v-model="form.lxrTel" />
          </el-form-item>
          <el-form-item label="电子邮箱">
            <el-input v-model="form.lxrEmail" />
          </el-form-item>
        </el-collapse-item>
        <el-form-item style="margin-left:0;text-align:center">
          <el-button type="primary" :loading="loading" @click="submitForm">保存</el-button>
          <el-button type="primary" :loading="loading" @click="reset">取消</el-button>
        </el-form-item>
      </el-collapse>
    </el-form>
  </div>
</template>

<script>
import { regionData, CodeToText } from 'element-china-area-data'
export default {
  data() {
    return {
      activeNames: ['1', '2', '3'],
      form: {},
      loading: false,
      areaData: regionData,
      url: '',
      urlList: {
        add: 'person/save',
        update: 'person/update',
        query: 'person/query'
      }

      // loginRules: {
      //   userCode: [{ required: true, trigger: 'blur', validator: validateUsername }],
      //   password: [{ required: true, trigger: 'blur', validator: validatePassword }]
      // }
    }
  },
  mounted() {
    this.query()
  },
  methods: {
    submitForm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.loading = true
          this.$store.dispatch(this.url, this.form).then(() => {
            this.loading = false
            this.query()
          }).catch(() => {
            this.loading = false
          })
        } else {
          console.log('error submit!!')
          return false
        }
      })
    },
    reset() {
      this.loading = true
      this.$store.dispatch(this.urlList.query, this.form).then((data) => {
        this.form = data
        if (data.userBasicInfoId) {
          this.url = this.urlList.update
        } else {
          this.url = this.urlList.add
        }
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    query() {
      this.$store.dispatch(this.urlList.query, this.form).then((data) => {
        this.form = data

        // 处理省市区
        const proviceSelect = []
        if (data.provinceCode) {
          proviceSelect.push(data.provinceCode)
        }
        if (data.cityCode) {
          proviceSelect.push(data.cityCode)
        }
        if (data.areaCode) {
          proviceSelect.push(data.areaCode)
        }
        this.$set(this.form, 'proviceSelect', proviceSelect)

        if (data.userBasicInfoId) {
          this.url = this.urlList.update
        } else {
          this.url = this.urlList.add
        }
      }).catch(() => {

      })
    },

    /**
     * 处理省市区选择
     */
    handleChange(val) {
      if (val) {
        if (val[0]) {
          this.form.province = CodeToText[val[0]]
          this.form.provinceCode = val[0]
        }
        if (val[1]) {
          this.form.city = CodeToText[val[1]]
          this.form.cityCode = val[1]
        }
        if (val[2]) {
          this.form.area = CodeToText[val[2]]
          this.form.areaCode = val[2]
        }
      }
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


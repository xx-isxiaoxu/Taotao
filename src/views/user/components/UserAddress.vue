<template>
  <div class="user-address">
    <div class="address-header">
      <el-button type="primary" @click="handleAddAddress">新增收货地址</el-button>
    </div>
    
    <el-table :data="addressList" style="width: 100%">
    <el-table-column label="收货信息" min-width="400">
      <template #default="{ row }">
        <div class="address-info">
          <span class="name">{{ row.receiver }}</span>
          <span class="phone">{{ row.phone }}</span>
          <span class="address">{{ formatAddress(row) }}</span>
        </div>
      </template>
    </el-table-column>
    <el-table-column prop="detail" label="详细地址" />
    <el-table-column label="操作" width="200">
    <template #default="{ row }">
      <el-button type="primary" link @click="handleEdit(row)">编辑</el-button>
      <el-button type="danger" link @click="handleDelete(row)">删除</el-button>
    </template>
  </el-table-column>
</el-table>

    <!-- 地址编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      title="添加收货地址"
      width="500px"
      :close-on-click-modal="false"
    >
      <el-form :model="addressForm" :rules="rules" ref="formRef" label-width="100px">
        <!-- 地址信息 -->
        <el-form-item label="地址信息" prop="region">
          <el-cascader
            v-model="addressForm.region"
            :options="regionOptions"
            placeholder="请选择省/市/区"
            clearable
            filterable
          />
        </el-form-item>
        <!-- 详细地址 -->
        <el-form-item label="详细地址" prop="address">
          <el-input
            v-model="addressForm.address"
            type="textarea"
            :rows="2"
            placeholder="请输入详细地址信息，如道路、门牌号、小区、楼栋号、单元等信息"
          />
        </el-form-item>
        <!-- 收货人姓名 -->
        <el-form-item label="收货人" prop="name">
          <el-input
            v-model="addressForm.name"
            maxlength="25"
            show-word-limit
            placeholder="长度不超过25个字符"
          />
        </el-form-item>
        <!-- 手机号 -->
        <el-form-item label="手机号" prop="phone">
          <el-select v-model="addressForm.phonePrefix" style="width: 110px; margin-right: 8px;">
            <el-option label="中国大陆 +86" value="+86" />
            <el-option label="中国香港 +852" value="+852" />
            <el-option label="中国澳门 +853" value="+853" />
            <el-option label="中国台湾 +886" value="+886" />
          </el-select>
          <el-input
            v-model="addressForm.phone"
            style="width: 220px"
            maxlength="11"
            placeholder="请输入手机号"
          />
        </el-form-item>
        <!-- 默认地址 -->
        <el-form-item>
          <el-checkbox v-model="addressForm.isDefault">设置为默认收货地址</el-checkbox>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSaveAddress">确认</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, watch, nextTick, onMounted } from 'vue'
import { useUserStore } from '@/stores/user'
import { ElMessage, ElMessageBox } from 'element-plus'
import { regionData, codeToText } from 'element-china-area-data'
import { getAddressList, addAddress, updateAddress, deleteAddress, setDefaultAddress } from '@/api/address'

const userStore = useUserStore()
const userId = userStore.userInfo.id
const addressList = ref([])
const dialogVisible = ref(false)
const dialogType = ref('add')
const addressForm = ref({
  region: [],
  address: '',
  name: '',
  phonePrefix: '+86',
  phone: '',
  isDefault: false,
  id: null
})
const formRef = ref(null)
const rules = {
  region: [{ required: true, message: '请选择省市区', trigger: 'change' }],
  address: [{ required: true, message: '请输入详细地址', trigger: 'blur' }],
  name: [{ required: true, message: '请输入收货人姓名', trigger: 'blur' }],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ]
}

// regionData 就是完整的省市区数据，直接赋值给 regionOptions 即可
const regionOptions = regionData

const fetchAddressList = async () => {
  if (!userId) return
  const res = await getAddressList(userId)
  addressList.value = res.data || []
}

const handleAddAddress = () => {
  dialogType.value = 'add'
  addressForm.value = { region: [], address: '', name: '', phonePrefix: '+86', phone: '', isDefault: false, id: null }
  dialogVisible.value = true
  nextTick(() => formRef.value && formRef.value.clearValidate())
}

const handleEdit = (row) => {
  dialogType.value = 'edit'
  addressForm.value = {
    region: [row.province, row.city, row.district].filter(Boolean),
    address: row.detail,
    name: row.receiver,
    phonePrefix: '+86',
    phone: row.phone,
    isDefault: row.isDefault,
    id: row.id
  }
  dialogVisible.value = true
  nextTick(() => formRef.value && formRef.value.clearValidate())
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该地址吗？', '提示', { type: 'warning' }).then(async () => {
    await deleteAddress(row.id)
    ElMessage.success('删除成功')
    fetchAddressList()
  })
}

const handleSaveAddress = async () => {
  try {
    await formRef.value.validate()
    const [province, city, district] = addressForm.value.region || []
    const payload = {
      userId,
      receiver: addressForm.value.name,
      phone: addressForm.value.phone,
      province,
      city,
      district,
      detail: addressForm.value.address,
      isDefault: addressForm.value.isDefault
    }
    if (dialogType.value === 'add') {
      await addAddress(payload)
    } else {
      await updateAddress(addressForm.value.id, { ...payload, id: addressForm.value.id })
    }
    dialogVisible.value = false
    ElMessage.success('保存成功')
    fetchAddressList()
  } catch (e) {}
}

const handleSetDefault = async (row) => {
  await setDefaultAddress(userId, row.id)
  ElMessage.success('设置默认地址成功')
  fetchAddressList()
}

function saveAddressList() {
  userStore.updateUserInfo({ addressList: addressList.value })
}

const formatAddress = (row) => {
  const regionText = [row.province, row.city, row.district]
    .map(code => codeToText[code] || code)
    .filter(Boolean)
    .join(' ')
  return `${regionText} ${row.detail || ''}`
}

const renderRoute = () => {
  const container = document.getElementById('amap-container')
  if (!container) {
    console.warn('amap-container 不存在，跳过地图渲染')
    return
  }
  // ...地图初始化代码
}

onMounted(fetchAddressList)
</script>

<style scoped>
.address-info {
  display: flex;
  align-items: center;
  gap: 16px;
}

.name {
  font-weight: bold;
  min-width: 80px;
}

.phone {
  color: #666;
  min-width: 120px;
}

.address {
  color: #333;
}
.address-header {
  margin-bottom: 20px;
}
.user-center {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
  background: #fff0f6;
  border-radius: 16px;
  box-shadow: 0 4px 24px 0 rgba(255, 192, 203, 0.15);
}
.user-center-card {
  min-height: 600px;
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 2px 8px 0 rgba(255, 192, 203, 0.08);
}
.header-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-bottom: 10px;
}
.el-button--primary {
  background-color: #ff69b4;
  border-color: #ff69b4;
}
.el-button--primary:hover {
  background-color: #ff85c0;
  border-color: #ff85c0;
}
.el-tabs__item.is-active {
  color: #ff69b4 !important;
}
.el-tabs__active-bar {
  background-color: #ff69b4 !important;
}
h2, h3 {
  color: #ff69b4;
}
.el-dialog__header {
  font-size: 22px;
  font-weight: bold;
  color: #222;
}
.el-form-item__label {
  font-weight: bold;
}
</style>
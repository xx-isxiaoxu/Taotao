<template>
  <div class="logistics-tracker">
    <el-timeline>
      <el-timeline-item
        v-for="(step, idx) in steps"
        :key="idx"
        :timestamp="step.time"
        :color="idx === steps.length - 1 ? '#ff69b4' : '#e1bee7'"
        placement="top"
      >
        <span class="desc" :class="{ active: idx === steps.length - 1 }">{{ step.desc }}</span>
      </el-timeline-item>
    </el-timeline>
  </div>
</template>

<script setup>
const props = defineProps({
  steps: {
    type: Array,
    default: () => []
  }
})
</script>

<style scoped>
.logistics-tracker {
  background: #fff0f6;
  border-radius: 16px;
  padding: 24px 32px;
  margin-bottom: 32px;
  box-shadow: 0 2px 12px rgba(255, 192, 203, 0.10);
}
.desc.active {
  color: #ff69b4;
  font-weight: bold;
}
</style>

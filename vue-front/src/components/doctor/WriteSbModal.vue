<template>
  <div>
    <b-button v-b-modal.modal-center>Launch centered modal</b-button>
    <b-modal id="modal-center" centered title="BootstrapVue" button-size="sm">
      <b-form-group
          label="Filter"
          label-for="filter-input"
          label-cols-sm="3"
          label-align-sm="right"
          label-size="sm"
          class="mb-0"
      >
        <b-input-group size="sm">
          <b-form-input
              id="filter-input"
              v-model="filter"
              type="search"
              placeholder="Type to Search"
          ></b-form-input>

          <b-input-group-append>
            <b-button :disabled="!filter" @click="filter = ''">Clear</b-button>
          </b-input-group-append>
        </b-input-group>
      </b-form-group>
    </b-modal>
  </div>
</template>

<script>
export default {
  name: "writeModel"
}
</script>

<style scoped>

</style>
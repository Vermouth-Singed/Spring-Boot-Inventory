<template>
  <v-container
    fill-height
    fluid
    grid-list-xl
  >
    <v-layout
      justify-center
      wrap
    >
      <v-flex
        md12
      >
        <material-card
          color="green"
          title="Inventory List"
          text="Filter, List, Add, Delete, File Upload"
        >
          <v-layout row wrap>
            <v-flex xs3>
              <v-combobox
                v-model="modelSelect"
                :items="modelItems"
                label="Model"
                multiple
                chips
              ></v-combobox>
            </v-flex>
            <v-flex xs3>
              <v-combobox
                v-model="makeSelect"
                :items="makeItems"
                label="Make"
                multiple
                chips
              ></v-combobox>
            </v-flex>
            <v-flex xs3>
              <v-combobox
                v-model="yearSelect"
                :items="yearItems"
                label="Year"
                multiple
                chips
              ></v-combobox>
            </v-flex>
            <v-flex xs3>
              <v-btn color="green darken-1" @click="fcUpdateFilter"><v-icon>mdi-arrow-right-drop-circle</v-icon>&ensp;Update Filter</v-btn>
            </v-flex>
          </v-layout>
          <div style="width:100%;height:20%;overflow-x:scroll;overflow-y:scroll;">
          <v-data-table
            :headers="headers"
            :items="filterItems"
            hide-actions
          >
            <template
              slot="headerCell"
              slot-scope="{ header }"
            >
              <span
                class="subheading font-weight-light text-success text--darken-3"
                v-text="header.text"
                v-if="header.value != ''"
              />
              <span
                class="subheading font-weight-light text-success text--darken-3"
                v-if="header.value == ''"
              >
                <v-checkbox v-model="allCheck" @change="fcCheckAll($event)"></v-checkbox>
              </span>
            </template>
            <template
              slot="items"
              slot-scope="{ item,index }"
            >
              <td><v-checkbox v-model="item.checked"></v-checkbox></td>
              <td class="text-xs-center">{{ index + 1 }}</td>
              <td class="text-xs-center">{{ item.vin }}</td>
              <td class="text-xs-center">{{ item.model }}</td>
              <td class="text-xs-center">{{ item.make }}</td>
              <td class="text-xs-center">{{ item.year }}</td>
              <td class="text-xs-center">{{ item.msrp }}</td>
              <td class="text-xs-center">{{ item.status }}</td>
              <td class="text-xs-center">{{ item.booked }}</td>
              <td class="text-xs-center">{{ item.listed }}</td>
            </template>
            <template v-slot:no-data>
              <v-alert :value="true" color="error" icon="warning">
                No Data
              </v-alert>
            </template>
          </v-data-table>
          </div>
          <v-layout row wrap>
            <v-flex xs6>
              <v-btn color="green darken-1" @click="dialog=true"><v-icon>mdi-cart-plus</v-icon>&ensp;Add</v-btn>
              <v-btn color="green darken-1" @click="fcMinusItem"><v-icon>mdi-trash-can</v-icon>&ensp;Delete</v-btn>
              <v-btn color="green darken-1" @click="fcUploadFile"><v-icon>mdi-cloud-upload</v-icon>&ensp;Upload File</v-btn>
            </v-flex>
          </v-layout>
        </material-card>
        <v-alert
          :value="uploadFileAlert"
          type="info"
          icon="mdi-watch"
        >
          Working on it...
        </v-alert>
      </v-flex>
      </v-layout>
      <v-layout row justify-center>
        <v-dialog v-model="dialog" max-width="400px">          
          <v-card>
            <v-card-title>
              <span class="headline">Add Inventory</span>
            </v-card-title>
            <v-card-text>
              <v-container grid-list-md>
                <v-layout wrap>
                  <v-flex xs12>
                    <v-text-field label="VIN#" :error-messages="noValueAlert&&inventoryForm.vin=='' ? 'required' : null" v-model="inventoryForm.vin"></v-text-field>
                  </v-flex>
                  <v-flex xs12>
                    <v-text-field label="Model" :error-messages="noValueAlert&&inventoryForm.model=='' ? 'required' : null" v-model="inventoryForm.model"></v-text-field>
                  </v-flex>
                  <v-flex xs12>
                    <v-text-field label="Make" :error-messages="noValueAlert&&inventoryForm.make=='' ? 'required' : null" v-model="inventoryForm.make"></v-text-field>
                  </v-flex>
                  <v-flex xs12>
                    <v-text-field label="Year" :error-messages="noValueAlert&&inventoryForm.year=='' ? 'required' : null" v-model="inventoryForm.year"></v-text-field>
                  </v-flex>
                  <v-flex xs12>
                    <v-text-field label="MSRP" :error-messages="noValueAlert&&inventoryForm.msrp=='' ? 'required' : null" v-model="inventoryForm.msrp"></v-text-field>
                  </v-flex>
                  <v-flex xs6>
                    <v-text-field label="Booked" :error-messages="noValueAlert&&inventoryForm.booked=='' ? 'required' : null" v-model="inventoryForm.booked"></v-text-field>
                  </v-flex>
                  <v-flex xs6>
                    <v-text-field label="Listed" :error-messages="noValueAlert&&inventoryForm.listed=='' ? 'required' : null" v-model="inventoryForm.listed"></v-text-field>
                  </v-flex>
                </v-layout>
              </v-container>
              <small></small>
            </v-card-text>
            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn color="blue darken-1" flat @click="fcAddItem" v-show="dontShowSave">Save</v-btn>
              <v-btn color="blue darken-1" flat @click="dialog = false">Cancel</v-btn>
            </v-card-actions>
          </v-card>
        </v-dialog>
      </v-layout>
    </v-container>
</template>

<script>
export default {
  data: () => ({
    allCheck:false,
    noValueAlert:false,
    uploadFileAlert:false,
    dialog:false,
    inventoryForm:{
      vin : "",
      model : "",
      make : "",
      year : "",
      msrp : "",
      status : "In stock",
      booked : "",
      listed : "",
      checked : false
    },
    modelSelect : [],
    makeSelect : [],
    yearSelect : [],
    modelItems : [],
    makeItems : [],
    yearItems : [],
    filterItems : [],
    headers: [      
      {
        sortable: false,
        text: '',
        input : 'asfsd',
        value: '',
        align: 'center'
      },
      {
        sortable: false,
        text: 'No',
        value: 'no',
        align: 'center'
      },
      {
        sortable: true,
        text: 'Vin#',
        value: 'vin',
        align: 'center'
      },
      {
        sortable: true,
        text: 'Model',
        value: 'model',
        align: 'center'
      },
      {
        sortable: true,
        text: 'Make',
        value: 'make',
        align: 'center'
      },
      {
        sortable: true,
        text: 'Year',
        value: 'year',
        align: 'center'
      },
      {
        sortable: true,
        text: 'MSRP',
        value: 'msrp',
        align: 'center'
      },
      {
        sortable: true,
        text: 'Status',
        value: 'status',
        align: 'center'
      },
      {
        sortable: true,
        text: 'Booked',
        value: 'booked',
        align: 'center'
      },
      {
        sortable: true,
        text: 'Listed',
        value: 'listed',
        align: 'center'
      }
    ],
    items: []
  }),
  computed : {
    dontShowSave : function(){
      for(let key in this.inventoryForm){
        if(this.inventoryForm[key] === ""){
          return false;
        }
      }

      return true;
    }
  },
  mounted : function(){
    this.$http({
      method : "GET",
      url : "/api/inventory"
    }).then(response => {
      this.items = response.data;

      this.fcUpdateSelect();
      this.fcUpdateFilter();
    });
  },
  methods: {
    fcUpdateFilter : function(){
      this.filterItems = [];

      this.items.forEach(element => {
        if(this.modelSelect.indexOf(element.model) > -1 && 
            this.makeSelect.indexOf(element.make) > -1 && 
            this.yearSelect.indexOf(element.year) > -1){
          this.filterItems = [...this.filterItems, element];
        }
      });
    },
    fcUpdateSelect : function(){
      this.modelSelect = [];
      this.makeSelect = [];
      this.yearSelect = [];
      this.modelItems = [];
      this.makeItems = [];
      this.yearItems = [];

      this.items.forEach(element=>{
        if(this.modelItems.indexOf(element.model) < 0)
          this.modelItems = [...this.modelItems, element.model];

        if(this.makeItems.indexOf(element.make) < 0)
          this.makeItems = [...this.makeItems, element.make];
        
        if(this.yearItems.indexOf(element.year) < 0)
          this.yearItems = [...this.yearItems, element.year];
      });

      this.modelSelect = [...this.modelItems];
      this.makeSelect = [...this.makeItems];
      this.yearSelect = [...this.yearItems];
    },
    fcAddItem : async function(){
      await this.$http({
        method : "GET",
        url : "/api/inventory/"+this.inventoryForm.vin
      }).then(response => {
        if(response.data != null && response.data.length > 0){
          alert("중복값이 존재합니다.");
          return;
        }
      });

      if(confirm("저장하시겠습니까?")){
        await this.$http({
          method : "POST",
          url : "/api/inventory",
          params : this.inventoryForm
        }).then(() => {
          alert("저장되었습니다.");

          this.inventoryForm = {
            vin : "",
            model : "",
            make : "",
            year : "",
            msrp : "",
            status : "In stock",
            booked : "",
            listed : ""
          };
  
          this.dialog = this.noValueAlert = this.allCheck = false;

          this.fcUpdateSelect();
          this.fcUpdateFilter();
        });
      }

    },
    fcMinusItem : function(){
      if(confirm("삭제하시겠습니까?")){
        this.filterItems.filter(item => item.checked).forEach(item => {
          this.$http({
            method : "DELETE",
            url : "/api/inventory/"+item.vin
          }).then(() => {
            this.noValueAlert = this.allCheck = false;

            this.fcUpdateSelect();
            this.fcUpdateFilter();
          });
        });
      }
    },
    fcUploadFile : function(){
      this.uploadFileAlert = true;

      setTimeout(()=>{
        this.uploadFileAlert = false;
      },1500);
    },
    fcCheckAll : function(event){
      this.filterItems.forEach(element=>{
        element.checked = event;
      });
    }
  }
}
</script>
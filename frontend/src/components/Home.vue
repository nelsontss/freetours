<template>
    <div>
        <div
        >
            <v-carousel
            cycle
            show-arrows-on-hover
            height = "400px"
            class = "carousel"
            >
                <v-carousel-item
                v-for="(slide, i) in slides"
                :key="i"
                >
                    <v-parallax
                            
                        :src="frontend_url + '/images/' + slide.image.image"
                    >
                        <v-row
                        align="center"
                        justify="center"
                        >
                       
                        <h1 class="display-1 font-weight-thin mb-4">{{slide.name}}</h1>
                        </v-row>
                    </v-parallax>
                </v-carousel-item>
            </v-carousel>
        </div>
        <v-row
        class = "filter_bar"
        width = 50%
        >
            <v-col
            :cols = 6
            >
            <v-autocomplete
                v-model = "sel_destination"
                :items = "all_destinations"
                item-text="name"
                item-value="name"
                label = "Destination"
                :rules = "[rules.required]"
                required
                outlined
                :append-icon="'mdi-map-marker'"
            >
            </v-autocomplete>
                
            </v-col>
            <v-col
            :cols = 2
            >
                <v-menu
                    v-model="menu2"
                    :close-on-content-click="false"
                    :nudge-right="40"
                    transition="scale-transition"
                    offset-y
                >
                    <template v-slot:activator="{ on }">
                    <v-text-field
                        v-model="date"
                        label="Tour Date"
                        :prepend-icon="'mdi-calendar'"
                        readonly
                        v-on="on"
                    ></v-text-field>
                    </template>
                    <v-date-picker v-model="date" @input="menu2 = false"></v-date-picker>
                </v-menu>
            </v-col>
            <v-col
            :cols = 2
            >
                <v-select
                v-model = "category"
                label = "Tour Category"
                :items = "categories"
                item-text="name"
                item-value="id"
                offset-y
                solo
                >
                </v-select>
            </v-col>
            <v-col
            :cols = 1
            >
                <v-btn
                class = "btn"
                v-on:click="goToSearch"
                >
                    Search Tour
                </v-btn>
            </v-col>
        </v-row>

        <h2>{{this.h2}}</h2>
        
        <!-- Cards -->
        <v-carousel
        hide-delimiters
        cycle
        show-arrows-on-hover
        height = "600px"
        class = "carousel"
        >
            <v-carousel-item 
            v-for="(chunk, i) in tours" 
            :key="i"
            class = "carousel_item"
            >
                <v-layout row>
                    <v-flex sm4 v-for="(tour, j) in chunk" :key="j" pl-2 pr-2>
                        <v-card
                        @click="$router.push('/tour/' + tour.id);"
                        class = "card"
                        width = "100%"
                        height = "100%"
                        >
                            <v-card-title
                            primary-title
                            class = "align-center"
                            >   
                                <div>
                                    <h3 class="headline mb-0">{{tour.city.name}}</h3>
                                </div>
                            </v-card-title>
                        
                            <v-img
                            :src="frontend_url + '/images/' + tour.images[0].image"
                            ></v-img>
                            
                            <v-card-subtitle>
                                {{tour.name}}
                            </v-card-subtitle>
                            <v-card-text>
                                {{tour.description}}
                            </v-card-text>
                        </v-card>
                    </v-flex>
                </v-layout>
            </v-carousel-item>
        </v-carousel>
    </div>
</template>

<script>
import CatService from '../services/cat_service'
import HomeService from '../services/home_service'
import HomeModel from '../models/home_model'
import User from '../models/user'
import {chunkArray} from './../utils/utils'
import store from '../store'

const FRONTEND_URL = process.env.VUE_APP_FRONTEND_URL

export default {
    name : "Home",
    data() {
        return {
            frontend_url: FRONTEND_URL,
            sel_destination : '',
            all_destinations: store.state.cities,
            /*all_destinations : [
                'Amsterdam, Netherlands',
                'Paris, France',
                'Lisbon, Portugal',
                'Porto, Portugal',
                'Faro, Portugal',
                'Braga, Portugal',
                'Guimarães, Portugal',
                'Famalicão, Portugal',
                'Funchal, Portugal'
            ],*/
            rules : {
                required: value => !!value || 'Required field.',
            },
            colors: [
            'indigo',
            'warning',
            'pink darken-2',
            'red lighten-1',
            'deep-purple accent-4',
            ],
            slides : [],
            tours : [],
            date: new Date().toISOString().substr(0, 10),
            categories : [],
            category : undefined,
            h2: 'Where will your next Tour be?',
            menu2: false,
        }
    },
    async created() {
        this.$store.subscribe( (mutation, state) => {
            if (mutation.type === 'setCities') {
                //console.log("updating")
                this.all_destinations = store.state.cities;
            }
        })

        var cat_resp = await CatService.get();
        if (cat_resp.status == 200) {
            this.categories = cat_resp.data;
        }
        else console.log('Cat_Response Status not 200')

        var home_response = await HomeService.getHome()
        this.slides = home_response.data.mostPopularCities
        if(home_response.data.nextTours.length){
            this.tours = chunkArray(home_response.data.nextTours, 3)
            this.h2 = 'Next tours:'
        }else {
            this.tours = chunkArray(home_response.data.suggestedTours, 3)
        } 

        console.log(this.tours[0].id + " " + this.tours[0].images)
        
    },
    methods: {
        goToSearch(){
            console.log("Aquiii")
            let params = {'fromDate': this.date}
            if(this.category != undefined)
                params['category'] = this.category
            if(this.sel_destination != undefined){
                params['destination'] = this.sel_destination
                this.$router.push({
                        name : 'Search', 
                        path:'/search', 
                        params
                    })
            }
        }
    }
}
</script

<style scoped>

h2 {
    margin-bottom: 25px;
}

.carousel {
    margin-bottom : 25px;
}

.card { 
    height : 600px;
}

.btn {
    margin-top : 5px;
}

.filter_bar {
    margin : auto;
}

.carousel_item {
    width : 90%;
    max-width : 90%;
    height : 600px;
    max-height : 600px;
    margin : auto;
}

</style>

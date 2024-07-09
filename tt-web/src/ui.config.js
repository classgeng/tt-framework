import Vue from 'vue'

Vue.prototype.$ui = {
  drawer: {
    width: {
      w480: '480px',
      w640: '640px',
      w720: '720px',
      w960: '960px',
      w1024: '1024px',
      w1080: '1080px',
      w1200: '1200px'
    }
  },
  table: {
    headerCellStyle: {
      backgroundColor: '#E9E9EB',
      color: '#282C34'
    }
  },
  form: {
    style: {
      tile: {
        width: '100%'
      }
    }
  },
  layout: {
    span: {
      one: 24,
      two: 12,
      three: 8,
      four: 6,
      six: 4,
      eight: 3,
      twelve: 2
    },
    gutter: {
      g10: 10,
      g20: 20,
      g30: 30
    }
  }
}

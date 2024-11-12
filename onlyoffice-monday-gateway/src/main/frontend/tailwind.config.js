/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ["../resources/templates/**/*.{html,js}"],
  theme: {
  	extend: {
  		colors: {
        	'login-blue': '#5299E0',
        	'secondary-dark': '#333333',
        	'input-dark': '#282828',
        	'background-dark': '#333333',
        	'subtext': '#A3A9AE',
       	},
    },
  },
  plugins: [],
}


/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ["../resources/templates/**/*.{html,js}"],
  theme: {
  	extend: {
  		colors: {
  			'base': '#FFFFFF',
  			'dark': '#333333',
        	'login-blue': '#5299E0',
        	'dark-login-blue': '#5299E0',
        	'secondary': '#FFFFFF',
        	'secondary-dark': '#333333',
        	'input': '#FFFFFF',
        	'input-border': '#D0D5DA',
        	'input-border-error': '#F21C0E',
        	'input-dark': '#282828',
        	'input-border-dark': '#474747',
        	'input-border-dark-error': '#E06451',
        	'color-disabled': '#F8F9F9',
        	'color-disabled-text': 'A3A9AE',
        	'color-disabled-dark': '#474747',
        	'color-disabled-dark-text': '#5C5C5C',
        	'color-generic-text': '#333333',
        	'color-generic-dark-text': '#FFFFFF',
        	'subtext': '#A3A9AE',
        	'subtext-dark': '#858585',
       	},
    },
  },
  plugins: [],
}
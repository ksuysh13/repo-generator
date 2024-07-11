/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/main/resources/templates/*.html",
    "./src/main/resources/templates/**/*.html"
  ],
  theme: {
    screens: {
      "sm": "640px",
      "md": "890px",
    },
    extend: {},
  },
  plugins: [
    require('daisyui')
  ],
  daisyui: {
    themes: ["retro", "synthwave"],
  },
}


(ns {{name}}.style
  "css creation with garden"
  (:refer-clojure :exclude [+ - * /])
  (:require [garden.def :refer [defstyles]]
            [garden.units :refer [px]]
            [garden.core :refer [css]]
            [garden.color :refer [hsl rgb]]
            [garden.arithmetic :refer [+ - * /]]
            [garden.stylesheet :refer [at-media]]
            [clojure.string :as str]

            [trowel.utils :refer :all]
            [trowel.tools :refer :all]))

(defstyles style
  (base)
  (colors {:white (rgb 255 255 255)
           :off-white (hsl 210 1 97)
           :gray (hsl 213 5 95)
           :black (rgb 0 0 0)
           :blue (rgb 79 158 247)
           :red (rgb 255 0 0)
           :red-light (hsl 360 40 90)
           :green (rgb 50 225 185)})
  (font-size {:small [(px 14) (px 18)]
              :msmall [(px 18) (px 23)]
              :medium [(px 22) (px 25)]
              :mlarge [(px 28) (px 30)]
              :large [(px 44) (px 50)]
              :xlarge [(px 64) (px 56)]
              :xxlarge [(px 100) (px 110)]})
  (border)
  (display)
  (spacing (grid 15 3 15 px))
  (nudge)
  (media-breaks)
  (interaction)
  [:body
   {:margin "0px"}])

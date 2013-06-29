// Comment to get more information during initialization
logLevel := Level.Warn

// The Typesafe repository 
resolvers += "Cloudbees Repository" at "https://repository-saucelabs.forge.cloudbees.com/release"




// Use the Play sbt plugin for Play projects
addSbtPlugin("play" % "sbt-plugin" % "2.1.1")
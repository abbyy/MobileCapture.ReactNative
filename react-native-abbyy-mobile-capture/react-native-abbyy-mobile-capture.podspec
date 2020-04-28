require "json"

package = JSON.parse(File.read(File.join(__dir__, "package.json")))

Pod::Spec.new do |s|
  s.name         = "react-native-abbyy-mobile-capture"
  s.version      = package["version"]
  s.summary      = package["description"]
  s.description  = <<-DESC
                  react-native-abbyy-mobile-capture
                   DESC
  s.homepage     = "https://github.com/abbyy/MobileCapture.ReactNative"
  s.license      = { :file => "LICENSE.md" }
  s.authors      = { "ABBYY" => "sdk@abbyy.com" }
  s.platforms    = { :ios => "11.0" }
  s.source       = { :git => "https://github.com/abbyy/MobileCapture.ReactNative.git", :tag => "#{s.version}" }

  s.source_files = "ios/**/*.{h,m,swift}"
  s.requires_arc = true

  s.dependency "React"

  #custom fields
  s.xcconfig = { 'FRAMEWORK_SEARCH_PATHS' => '"${PROJECT_DIR}/../libs"' }
end


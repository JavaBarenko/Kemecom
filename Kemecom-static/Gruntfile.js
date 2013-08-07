module.exports = function(grunt) {
    grunt.initConfig({
        buildDir: '/usr/local/Cellar/jetty/9.0.3/libexec/webapps.demo/ROOT', //'../Kemecom-web/src/main/webapp/pages',
        distDir: '../Kemecom-web/src/main/webapp/pages',
        clean: {
            build: ['<%= buildDir %>', 'report'],
            dist: ['dist', 'report']
        },
        less: {
            options: {
                paths: ["src/less"]
            },
            build: {
                files: [{
                    '<%= buildDir %>/assets/style.css': ['src/less/*.less'],
                    '<%= buildDir %>/assets/k-welcome.css': ['src/less/webComponents/k-welcome.less'],
                    '<%= buildDir %>/assets/k-sign-in.css': ['src/less/webComponents/k-sign-in.less'],
                    '<%= buildDir %>/assets/k-remember-password.css': ['src/less/webComponents/k-remember-password.less'],
                    '<%= buildDir %>/assets/k-profile.css': ['src/less/webComponents/k-profile.less'],
                    '<%= buildDir %>/assets/k-login.css': ['src/less/webComponents/k-login.less'],
                    '<%= buildDir %>/assets/k-facebook.css': ['src/less/webComponents/k-facebook.less']
                }]
            },
            dist: {
                options: {
                    yuicompress: true,
                    strictImport: true,
                    report: 'gzip'
                },
                files: {
                    'dist/assets/style.css': ['src/less/**/*.less']
                }
            }
        },
        csslint: {
            options: {
                import: 2,
                absoluteFilePathsForFormatters: true,
                formatters: [{
                        id: 'text',
                        dest: 'report/csslint.txt'
                    }, {
                        id: 'csslint-xml',
                        dest: 'report/csslint.xml'
                    }]
            },
            build: {
                src: ['<%= buildDir %>/assets/*.css']
            },
            dist: {
                src: ['dist/assets/*.css']
            }
        },
        cssmin: {
            options: {
                banner: '/*! <%= pkg.name %> - v<%= pkg.version %> - <%= grunt.template.today("yyyy-mm-dd") %> */'
            },
            build: {
                files: {
                    '<%= buildDir %>/assets/style.css': ['src/css/**/*.css', '<%= buildDir %>/assets/style.css']
                }
            },
            dist: {
                files: {
                    'dist/assets/style.css': ['src/css/**/*.css', 'dist/assets/style.css']
                }
            }
        },
        jshint: {
            all: ['{src, test}/js/*.js']
        },
        pkg: grunt.file.readJSON('package.json'),
        uglify: {
            options: {
                banner: '/*! <%= pkg.name %> - v<%= pkg.version %> - <%= grunt.template.today("yyyy-mm-dd") %> */'
            },
            build: {
                options: {
                    mangle: false,
                    beautify: true
                },
                files: {
                    '<%= buildDir %>/assets/script.js': [
                        'src/js/k.js', 'src/js/zipCode.js'
                    ]
                }
            },
            dist: {
                options: {
                    report: 'gzip',
                    beautify: false
                },
                files: {
                    'dist/assets/script.js': ['src/js/main.js', 'src/js/other.js']
                }
            }
        },
        htmlmin: {
            options: {
                removeAttributeQuotes: true,
                removeRedundantAttributes: true,
                useShortDoctype: true,
                removeOptionalTags: true
            },
            build: {
                files: [{
                        expand: true,
                        cwd: 'src/',
                        src: ['*.html'],
                        dest: '<%= buildDir %>/'
                    },{
                    expand: true,
                    cwd: 'src/webComponents',
                    src: ['*.html'],
                    dest: '<%= buildDir %>/webComponents'
                }]
            },
            dist: {
                options: {
                    removeComments: true,
                    removeCommentsFromCDATA: true,
                    collapseWhitespace: true
                },
                files: [{
                        expand: true,
                        cwd: 'src/',
                        src: ['*.html'],
                        dest: 'dist/'
                    },{
                    expand: true,
                    cwd: 'src/webComponents',
                    src: ['*.html'],
                    dest: 'dist/webComponents'
                }]
            }
        },
        imagemin: {
            options: {
                progressive: true
            },
            build: {
                options: {
                    optimizationLevel: 0
                },
                files: [{
                        expand: true,
                        cwd: 'src/img/',
                        src: ['**/*.jpg', '**/*.png', '**/*.jpeg', '**/*.gif'],
                        dest: '<%= buildDir %>/assets/'
                    }]
            },
            dist: {
                options: {
                    optimizationLevel: 3
                },
                files: [{
                        expand: true,
                        cwd: 'src/img/',
                        src: ['**/*.jpg', '**/*.png', '**/*.jpeg', '**/*.gif'],
                        dest: 'dist/assets/'
                    }]
            }
        },
        concat: {
            options: {
                separator: ';',
                banner: '/*! <%= pkg.name %> - v<%= pkg.version %> - ' + '<%= grunt.template.today("yyyy-mm-dd") %> */'
            },
            build: {
                src: ['src/js/lib/polymer.min.js'],
                dest: '<%= buildDir %>/assets/polymer.js'
            },
            dist: {
                src: ['src/js/lib/polymer.min.js'],
                dest: 'dist/assets/lib.js'
            }
        },
        copy:{
            build:{
                files:[
                    {
                        expand:true,
                        src:'src/js/lib/*',
                        flatten:true,
                        dest: '<%= buildDir %>/assets/',
                        filter: 'isFile'
                    }
                ]
            }
        },
        watch: {
            options: {
                nospawn: true
                //livereload: 34567 //port: 35729
            },
            buildScripts: {
                files: ['{src,test}/js/**/*.js', 'Gruntfile.js'],
                tasks: ['jshint', 'uglify:build']
            },
            buildImages: {
                files: ['src/img/**/*'],
                tasks: ['imagemin:build']
            },
            buildStyles: {
                files: ['src/less/**/*.less'],
                tasks: ['less:build', 'csslint:build']
            },
            buildPages: {
                files: ['src/**/*.html'],
                tasks: ['htmlmin:build']
            }
        }
    });

    //configura watchers
    grunt.event.on('watch', function(action, filepath) {
        grunt.config(['jshint', 'all'], filepath);
        grunt.config(['uglify:build', 'all'], filepath);
        grunt.config(['imagemin:build', 'all'], filepath);
        grunt.config(['less:build', 'all'], filepath);
        grunt.config(['csslint:build', 'all'], filepath);
        grunt.config(['htmlmin:build', 'all'], filepath);
    });


    // carrega plugins
    grunt.loadNpmTasks('grunt-contrib-clean');
    grunt.loadNpmTasks('grunt-contrib-less');
    grunt.loadNpmTasks('grunt-contrib-jshint');
    grunt.loadNpmTasks('grunt-contrib-uglify');
    grunt.loadNpmTasks('grunt-contrib-csslint');
    grunt.loadNpmTasks('grunt-contrib-cssmin');
    grunt.loadNpmTasks('grunt-contrib-htmlmin');
    grunt.loadNpmTasks('grunt-contrib-imagemin');
    grunt.loadNpmTasks('grunt-contrib-watch');
    grunt.loadNpmTasks('grunt-contrib-concat');
    grunt.loadNpmTasks('grunt-contrib-copy');

    //registra tarefas
    grunt.registerTask('build', ['clean:build', 'less:build', 'csslint:build', 'cssmin:build', 'jshint', 'uglify:build', 'copy:build', 'htmlmin:build', 'imagemin:build']);
    grunt.registerTask('dist', ['clean:dist', 'less:dist', 'csslint:dist', 'cssmin:dist', 'jshint', 'uglify:dist', 'copy:dist', 'htmlmin:dist', 'imagemin:dist']);
    grunt.registerTask('default', ['build']);
};
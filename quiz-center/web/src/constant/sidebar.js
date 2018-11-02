export default [{
    name: '全部',
    icon: 'appstore-o',
    linkTo: ['/quizzes']
},
    {
        name: '题组管理',
        icon: 'switcher',
        linkTo: ['/quizGroups']
    },
    {
        name: '简单客观题',
        icon: 'folder-open',
        linkTo: [],
        subMenu: [
            {
                index: 117,
                name: '单选题',
                icon: 'file-text',
                linkTo: ['/singleChoiceQuizzes', '/singleChoiceQuizzes/:id/editor', '/singleChoiceQuizzes/new']
            }, {
                index: 118,
                name: '多选题',
                icon: 'file',
                linkTo: ['/multipleChoiceQuizzes', '/multipleChoiceQuizzes/:id/editor', '/multipleChoiceQuizzes/new']
            }, {
                index: 119,
                name: '填空题',
                icon: 'switcher',
                linkTo: ['/blankQuizzes', '/blankQuizzes/:id/editor', '/blankQuizzes/new']
            }
        ]
    },
    {
        name: '主观题',
        icon: 'book',
        linkTo: ['/subjectiveQuizzes', '/subjectiveQuizzes/:id/editor', '/subjectiveQuizzes/new']
    }, {
        name: 'TW逻辑题',
        icon: 'star-o',
        linkTo: ['/logicQuizzes', '/logicQuizzes/:id/editor', '/logicQuizzes/new']
    }, {
        name: '单栈编程题',
        icon: 'code-o',
        linkTo: ['/codingQuizzes', '/codingQuizzes/:id/editor', '/codingQuizzes/new']
    }, {
        name: '多语言在线编程题',
        icon: 'code-o',
        linkTo: ['/onlineCodingQuizzes', '/onlineCodingQuizzes/:id/editor', '/onlineCodingQuizzes/new']
    }, {
        name: '单语言在线编程题',
        icon: 'code-o',
        linkTo: ['/onlineLanguageQuizzes', '/onlineLanguageQuizzes/:id/editor', '/onlineLanguageQuizzes/new']
    }, {
        name: '技术栈管理',
        icon: 'bars',
        linkTo: ['/stacks', '/stacks/new']
    }, {
        name: '标签管理',
        icon: 'tag-o',
        linkTo: ['/tags', '/tags/:id/editor', '/tags/new']
    }]

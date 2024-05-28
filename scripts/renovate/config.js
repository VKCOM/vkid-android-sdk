module.exports = {
    endpoint: 'https://' + process.env.VKID_GITLAB_HOST + '/api/v4/',
    gitAuthor: 'Klimchuk Daniil <daniil.klimchuk@vk.team>',
    hostRules: [
        {
            matchHost: 'github.com',
            token: process.env.VKID_GITHUB_TOKEN,
        },
        {
            matchHost: process.env.VKID_GITLAB_HOST,
            token: process.env.VKID_GITLAB_TOKEN,
        },
    ],
    platform: 'gitlab',
    requireConfig: 'required',
    token: process.env.VKID_GITLAB_TOKEN,
    repositories: ['vk-ecosystem/vk-id-sdk/android-sdk'],

    // Group all dependency updates to single mr
    "groupName": "all dependencies",
    "groupSlug": "all",
    "lockFileMaintenance": {
        "enabled": true
    },
    "packageRules": [
      {
        "groupName": "all dependencies",
        "groupSlug": "all",
        "matchPackagePatterns": [
          "*",
        ]
      },
    ],

    "separateMajorMinor": false,

    "assignees": [
	    "@daniil.klimchuk"
    ],
    "reviewers": [
	    "@s.golovin"
    ],

    "draftPR": true,

    "baseBranches": ["develop"],
    "commitMessagePrefix": "VKIDSDK-0: ",
    "branchName": "task/VKIDSDK-0/renovate-all"
}

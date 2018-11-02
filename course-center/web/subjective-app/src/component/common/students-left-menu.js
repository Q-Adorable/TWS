import React, { Component } from 'react'
import { Menu } from 'antd'
import { connect } from 'react-redux'
import '../../less/index.less'
import * as actions from '../../actions/assignment'
import { withRouter } from 'react-router-dom'

class StudentLeftMenu extends Component {
    constructor (props) {
        super(props)
        this.state = {
            assignmentId: -1,
            programId: -1,
            taskId: -1,
            quizId: -1
        }
    }

    componentDidMount () {
        this.setState({
            assignmentId: this.props.match.params.assignmentId,
            programId: this.props.match.params.programId,
            taskId: this.props.match.params.taskId,
            quizId: this.props.match.params.quizId
        }, () => {
            this.props.getTutorFollowStudent(this.state.programId)
        })
    }

    handleClick (e) {
        this.setState({quizId: parseInt(e.key)})
        this.props.clickQuiz(parseInt(e.key))
    }

    render () {
        const {assignmentQuizzesData} = this.props
        const myStudent = assignmentQuizzesData.myStudent
        return (
            <Menu selectedKeys={[`${this.props.studentId}`]} onClick={this.handleClick.bind(this)}
                  mode='inline'
                  style={{
                      width: 240
                  }}
            >{
                myStudent.map((data) =>
                    <Menu.Item key={data.student.id} >
                        {data.student.name}(@{data.student.username})
                    </Menu.Item>)
            }
            </Menu>
        )
    }
}

const mapStateToProps = ({assignmentQuizzesData}) => ({
    assignmentQuizzesData
})

const mapDispatchToProps = dispatch => ({
    getTutorFollowStudent: (assignmentId) => dispatch(actions.getTutorFollowStudent(assignmentId))
})
export default withRouter(connect(mapStateToProps, mapDispatchToProps)(StudentLeftMenu))

package scripts.mocks
import csw.params.commands.CommandResponse
import csw.params.core.models.Id
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import org.scalatest.Matchers
import org.scalatest.mockito.MockitoSugar
import tmt.ocs.api.SequenceFeeder
import tmt.ocs.models.{AggregateResponse, CommandList}

import scala.concurrent.Future

object SequenceFeederMock extends Matchers with MockitoSugar {
  def createMock: SequenceFeeder = {
    val mockSequenceFeeder     = mock[SequenceFeeder]
    val dummyAggregateResponse = AggregateResponse(CommandResponse.Completed(Id("dummy-id")))

    when(mockSequenceFeeder.feed(any[CommandList])).thenReturn(Future.successful(()))
    when(mockSequenceFeeder.submit(any[CommandList])).thenReturn(Future.successful(dummyAggregateResponse))

    mockSequenceFeeder
  }
}

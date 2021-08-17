import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ReportingActivityDetailComponent } from './reporting-activity-detail.component';

describe('Component Tests', () => {
  describe('ReportingActivity Management Detail Component', () => {
    let comp: ReportingActivityDetailComponent;
    let fixture: ComponentFixture<ReportingActivityDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ReportingActivityDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ reportingActivity: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ReportingActivityDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ReportingActivityDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load reportingActivity on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.reportingActivity).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ActivityFaDetailComponent } from './activity-fa-detail.component';

describe('Component Tests', () => {
  describe('ActivityFa Management Detail Component', () => {
    let comp: ActivityFaDetailComponent;
    let fixture: ComponentFixture<ActivityFaDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ActivityFaDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ activity: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ActivityFaDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ActivityFaDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load activity on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.activity).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});

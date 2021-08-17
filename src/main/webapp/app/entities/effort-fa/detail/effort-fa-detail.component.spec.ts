import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EffortFaDetailComponent } from './effort-fa-detail.component';

describe('Component Tests', () => {
  describe('EffortFa Management Detail Component', () => {
    let comp: EffortFaDetailComponent;
    let fixture: ComponentFixture<EffortFaDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [EffortFaDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ effort: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(EffortFaDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(EffortFaDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load effort on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.effort).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});

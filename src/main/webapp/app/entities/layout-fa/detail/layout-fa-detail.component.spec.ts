import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LayoutFaDetailComponent } from './layout-fa-detail.component';

describe('Component Tests', () => {
  describe('LayoutFa Management Detail Component', () => {
    let comp: LayoutFaDetailComponent;
    let fixture: ComponentFixture<LayoutFaDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [LayoutFaDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ layout: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(LayoutFaDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(LayoutFaDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load layout on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.layout).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});

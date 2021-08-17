import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { LayoutService } from '../service/layout.service';

import { LayoutComponent } from './layout.component';

describe('Component Tests', () => {
  describe('Layout Management Component', () => {
    let comp: LayoutComponent;
    let fixture: ComponentFixture<LayoutComponent>;
    let service: LayoutService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [LayoutComponent],
      })
        .overrideTemplate(LayoutComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LayoutComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(LayoutService);

      const headers = new HttpHeaders().append('link', 'link;link');
      jest.spyOn(service, 'query').mockReturnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.layouts?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
